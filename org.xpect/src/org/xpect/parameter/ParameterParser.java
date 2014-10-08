/*******************************************************************************
 * Copyright (c) 2012 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.xpect.parameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.util.Pair;
import org.eclipse.xtext.util.Strings;
import org.eclipse.xtext.util.formallang.FollowerFunctionImpl;
import org.eclipse.xtext.util.formallang.Nfa;
import org.eclipse.xtext.util.formallang.NfaUtil;
import org.eclipse.xtext.util.formallang.NfaUtil.BacktrackHandler;
import org.eclipse.xtext.util.formallang.StringProduction;
import org.eclipse.xtext.util.formallang.StringProduction.ProdElement;
import org.xpect.XjmXpectMethod;
import org.xpect.XpectInvocation;
import org.xpect.parameter.IParameterParser.IMultiParameterParser;
import org.xpect.parameter.IParameterParser.IParsedParameterProvider;
import org.xpect.parameter.IParameterParser.MultiParameterParser;
import org.xpect.parameter.ParameterParser.ParameterParserImpl;
import org.xpect.text.IRegion;
import org.xpect.text.Region;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
@SuppressWarnings("restriction")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@MultiParameterParser(ParameterParserImpl.class)
/**
 * Parses text between method name and expectation separator ('---' or '-->') in order to
 * provide arguments for the test. Syntax similar to Xtext, including alternatives etc.
 *  
 *  
 */
public @interface ParameterParser {
	public static class AssignedProduction extends StringProduction {

		public AssignedProduction(String production) {
			super(production);
		}

		@Override
		protected ProdElement parsePrim(Stack<Pair<Token, String>> tokens) {
			Pair<Token, String> current = tokens.pop();
			switch (current.getFirst()) {
			case PL:
				ProdElement result1 = parseAlt(tokens);
				if (tokens.peek().getFirst().equals(Token.PR))
					tokens.pop();
				else
					throw new RuntimeException("')' expected, but " + tokens.peek().getFirst() + " found");
				parseCardinality(tokens, result1);
				return result1;
			case STRING:
				ProdElement result2 = createElement(ElementType.TOKEN);
				result2.setValue(current.getSecond());
				parseCardinality(tokens, result2);
				return result2;
			case ID:
				ProdElement result3 = createElement(ElementType.TOKEN);
				result3.setName(current.getSecond());
				Pair<Token, String> eq = tokens.pop();
				if (eq.getFirst() == Token.EQ) {
					Pair<Token, String> val = tokens.pop();
					switch (val.getFirst()) {
					case ID:
					case STRING:
						result3.setValue(val.getSecond());
						break;
					default:
						throw new RuntimeException("Unexpected token " + current.getFirst());
					}
				} else
					throw new RuntimeException("Unexpected token " + eq.getFirst() + ", expected '='");
				parseCardinality(tokens, result3);
				return result3;
			default:
				throw new RuntimeException("Unexpected token " + current.getFirst());
			}
		}
	}

	public static class ParameterParserImpl implements IMultiParameterParser {

		protected static class BacktrackItem {
			protected int offset;
			protected ProdElement token;
			protected String value;

			public BacktrackItem(int offset) {
				super();
				this.offset = offset;
			}

			public BacktrackItem(int offset, ProdElement token, String value) {
				super();
				this.offset = offset;
				this.token = token;
				this.value = value;
			}

			@Override
			public String toString() {
				return token + ":" + value;
			}
		}

		protected static final Pattern WS = Pattern.compile("^[\\s]+");

		private final ParameterParser annotation;

		public ParameterParserImpl(ParameterParser annotation) {
			this.annotation = annotation;
		}

		protected List<IParsedParameterProvider> mapNameToIndex(XpectInvocation invocation, Map<String, IParsedParameterProvider> params) {
			XjmXpectMethod method = (XjmXpectMethod) invocation.getMethod();
			IParsedParameterProvider[] result = new IParsedParameterProvider[method.getParameterCount()];
			for (int i = 0; i < result.length; i++)
				result[i] = params.get("arg" + i);
			return Arrays.asList(result);
		}

		public IRegion claimRegion(XpectInvocation invocation) {
			INode node = NodeModelUtils.getNode(invocation);
			int start = node.getOffset() + node.getLength();
			int end = invocation.getFile().getDocument().indexOf(annotation.endToken(), start);
			if (end < 0)
				end = invocation.getFile().getDocument().length();
			return new Region(start, end - start);
		}

		protected IParsedParameterProvider convertValue(XpectInvocation invocation, Token token, String value, IRegion claim, int offset) {
			Region semantic = new Region(offset, value.length());
			switch (token) {
			case OFFSET:
				return new MatchingOffsetProvider(invocation, value, claim, semantic);
			case INT:
				return new ParsedIntegerProvider(value, claim, semantic);
			case ID:
			case STRING:
			case TEXT:
				return new ParsedParameterProvider(new String(value), claim, semantic);
			}
			throw new RuntimeException();
		}

		protected IRegion findParameterRegion(XpectInvocation invocation, List<IClaimedRegion> claims) {
			int start = 0, end = 0;
			List<IClaimedRegion> otherClaims = Lists.newArrayList();
			for (IClaimedRegion claim : claims)
				if (claim.getClaminer() == this) {
					start = claim.getOffset();
					end = start + claim.getLength();
				} else
					otherClaims.add(claim);
			for (IClaimedRegion claim : otherClaims)
				if (end > claim.getOffset())
					end = claim.getOffset();
			return new Region(start, end - start);
		}

		public ParameterParser getAnnotation() {
			return annotation;
		}

		protected Nfa<ProdElement> getParameterNfa(String syntax) {
			AssignedProduction prod = new AssignedProduction(syntax);
			FollowerFunctionImpl<ProdElement, String> ff = new FollowerFunctionImpl<ProdElement, String>(prod);
			ProdElement start = prod.new ProdElement(StringProduction.ElementType.TOKEN);
			ProdElement stop = prod.new ProdElement(StringProduction.ElementType.TOKEN);
			Nfa<ProdElement> result = new NfaUtil().create(prod, ff, start, stop);
			return result;
		}

		protected Map<String, IParsedParameterProvider> parseParams(String paramSyntax, XpectInvocation invocation, IRegion claim) {
			if (Strings.isEmpty(paramSyntax))
				return Collections.emptyMap();
			String document = invocation.getFile().getDocument();
			final String text = document.substring(claim.getOffset(), claim.getOffset() + claim.getLength());
			Nfa<ProdElement> nfa = getParameterNfa(paramSyntax);
			Matcher ws = WS.matcher(text);
			List<BacktrackItem> trace = new NfaUtil().backtrack(nfa, new BacktrackItem(ws.find() ? ws.end() : 0),
					new BacktrackHandler<ProdElement, BacktrackItem>() {
						public BacktrackItem handle(ProdElement state, BacktrackItem previous) {
							if (Strings.isEmpty(state.getValue()))
								return new BacktrackItem(previous.offset, state, state.getValue());
							if (Strings.isEmpty(state.getName())) {
								if (text.regionMatches(previous.offset, state.getValue(), 0, state.getValue().length())) {
									int newOffset = previous.offset + state.getValue().length();
									Matcher ws = WS.matcher(text).region(newOffset, text.length());
									int childOffset = ws.find() ? ws.end() : newOffset;
									return new BacktrackItem(childOffset, state, state.getValue());
								}
							} else {
								Token t = Token.valueOf(state.getValue());
								Matcher matcher = t.pattern.matcher(text).region(previous.offset, text.length());
								if (matcher.find()) {
									Matcher ws = WS.matcher(text).region(matcher.end(), text.length());
									int childOffset = ws.find() ? ws.end() : matcher.end();
									String value = matcher.groupCount() > 0 && matcher.group(1) != null ? matcher.group(1) : matcher
											.group(0);
									return new BacktrackItem(childOffset, state, value);
								}
							}
							return null;
						}

						public boolean isSolution(BacktrackItem result) {
							return true;
						}

						public Iterable<ProdElement> sortFollowers(BacktrackItem result, Iterable<ProdElement> followers) {
							return followers;
						}
					});
			Map<String, IParsedParameterProvider> result = Maps.newHashMap();
			int offset = 0;
			if (trace != null && !trace.isEmpty()) {
				for (BacktrackItem item : trace) {
					if (item.token != null && item.token.getName() != null) {
						String key = item.token.getName();
						Token token = Token.valueOf(item.token.getValue());
						result.put(key, convertValue(invocation, token, item.value, claim, claim.getOffset() + offset));
					}
					offset = item.offset;
				}
				return result;
			}
			throw new RuntimeException("could not parse '" + text + "' with grammar '" + paramSyntax + "'");
		}

		public List<IParsedParameterProvider> parseRegion(XpectInvocation invocation, List<IClaimedRegion> claims) {
			IRegion claim = findParameterRegion(invocation, claims);
			Map<String, IParsedParameterProvider> parsedParams = parseParams(annotation.syntax(), invocation, claim);
			return mapNameToIndex(invocation, parsedParams);
		}
	}

	public static class MatchingOffsetProvider extends AbstractOffsetProvider implements IParsedParameterProvider {
		protected final XpectInvocation invocation;
		protected final String value;
		protected final IRegion claim;
		protected final IRegion semantic;

		public MatchingOffsetProvider(XpectInvocation invocation, String value, IRegion claim, IRegion semantic) {
			super();
			this.invocation = invocation;
			this.value = value;
			this.claim = claim;
			this.semantic = semantic;
		}

		public XpectInvocation getInvocation() {
			return invocation;
		}

		public String getStringValue() {
			return value;
		}

		@Override
		public int getOffset() {
			String val = value;
			int add = val.indexOf('|');
			if (add >= 0)
				val = val.substring(0, add) + val.substring(add + 1);
			else
				add = 0;
			INode node = NodeModelUtils.getNode(invocation);
			int offset = node.getOffset() + node.getLength();
			for (IParameterProvider param : invocation.getParameters())
				if (param instanceof IParsedParameterProvider) {
					IRegion claimedRegion = ((IParsedParameterProvider) param).getClaimedRegion();
					int regionEnd = claimedRegion.getOffset() + claimedRegion.getLength();
					if (regionEnd > offset)
						offset = regionEnd;
				}
			String text = invocation.getFile().getDocument();
			int result = text.indexOf(val, offset);
			if (result >= 0) {
				int off = result + add;
				return off;
			} else
				throw new RuntimeException("OFFSET '" + val + "' not found");
		}

		public IRegion getClaimedRegion() {
			return claim;
		}

		public List<IRegion> getSemanticRegions() {
			return Collections.singletonList(semantic);
		}

	}

	public enum Token {
		/**
		 * String
		 */
		ID("[a-zA-Z][a-zA-Z0-9_]*"), //
		/**
		 * Integer
		 */
		INT("[0-9]+"), //
		/**
		 * Integer, returns position of first occurrence of pattern below the test 
		 */
		OFFSET("'([^']*)'|[^\\s]+"), //
		/**
		 * String, returns the string inside the quotes.
		 */
		STRING("'([^']*)'"), //
		/**
		 * String, similar to ID but accepts more characters, i.e. anything that is not whitespace
		 */
		TEXT("[^\\s]+");

		public final Pattern pattern;

		private Token(String regex) {
			this.pattern = Pattern.compile("^" + regex);
		}
	}

	String endToken() default "\n";

	String syntax() default "";

}
