/*******************************************************************************
 * Copyright (c) 2012-2023 TypeFox GmbH and itemis AG.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 *   Tobias Jeske - Update to Xtext 2.31
 *******************************************************************************/
package org.eclipse.xpect.mwe2

import de.itemis.statefullexer.ILexerStatesProvider
import de.itemis.statefullexer.LexerStatesProvider
import de.itemis.statefullexer.Token
import java.util.Set
import org.eclipse.xtend.lib.annotations.Data
import org.eclipse.xtend2.lib.StringConcatenationClient
import org.eclipse.xtext.AbstractRule
import org.eclipse.xtext.Grammar
import org.eclipse.xtext.TerminalRule
import org.eclipse.xtext.parser.antlr.Lexer
import org.eclipse.xtext.xtext.generator.model.GuiceModuleAccess
import org.eclipse.xtext.xtext.generator.model.IXtextGeneratorFileSystemAccess
import org.eclipse.xtext.xtext.generator.model.TypeReference
import org.eclipse.xtext.xtext.generator.parser.antlr.AntlrGrammarGenUtil
import org.eclipse.xtext.xtext.generator.parser.antlr.KeywordHelper
import org.eclipse.xtext.xtext.generator.parser.antlr.ex.ExternalAntlrLexerFragment

import static extension org.eclipse.xtext.GrammarUtil.*
import static extension org.eclipse.xtext.util.Strings.*
import static extension org.eclipse.xtext.xtext.generator.parser.antlr.TerminalRuleToLexerBody.*

class StatefulLexerFragment extends ExternalAntlrLexerFragment {
	override protected improveLexerCodeQuality(IXtextGeneratorFileSystemAccess fsa, TypeReference lexer) {
		// ignore
	}

	override protected normalizeTokens(IXtextGeneratorFileSystemAccess fsa, String tokenFile) {
		// ignore
	}

	override protected suppressWarnings(IXtextGeneratorFileSystemAccess fsa, TypeReference type) {
		// ignore
	}

	override protected normalizeLineDelimiters(IXtextGeneratorFileSystemAccess fsa, TypeReference type) {
		// ignore
	}

	override void doGenerate() {
		val nfa = new LexerStatesProvider().getStates(getGrammar)
		val srcGenFsa = if (runtime) {
				this.getProjectConfig().getRuntime().getSrcGen()
			} else {
				this.getProjectConfig().genericIde.srcGen
			}

		val grammar = this.grammar
		lexerGrammar = grammar.namespace + ".lexer." + grammar.name.lastToken(".") +
			(if(runtime) "RT" else if(contentAssist) "CA" else "HI")
		val grammarFile = lexerGrammar.replace('.', '/') + ".g";

		srcGenFsa.generateFile(grammarFile, genLexer(grammar, nfa).toString)

		doSuperGenerate()
	}

	def doSuperGenerate() {
		if (runtime) {
			val binding = new StringConcatenationClient() {
				protected override void appendTo(TargetStringConcatenation target) {
					target.append(
						"binder.bind(" + Lexer.getName() + ".class)" +
							".annotatedWith(com.google.inject.name.Names.named(" +
							"org.eclipse.xtext.parser.antlr.LexerBindings.RUNTIME" + ")).to(" + lexerGrammar +
							".class);");
				}
			};
			new GuiceModuleAccess.BindingFactory().addConfiguredBinding("RuntimeLexer", binding).contributeTo(
				getLanguage().getRuntimeGenModule());
		}
		if (highlighting) {
			if (this.getProjectConfig().getEclipsePlugin().getRoot() !== null) {
				val binding = new StringConcatenationClient() {
					protected override void appendTo(TargetStringConcatenation target) {
						target.append(
							"binder.bind(" + Lexer.getName() + ".class)" +
								".annotatedWith(com.google.inject.name.Names.named(" +
								"org.eclipse.xtext.ide.LexerIdeBindings.HIGHLIGHTING" + ")).to(" + lexerGrammar +
								".class);");
					}
				};
				new GuiceModuleAccess.BindingFactory().addConfiguredBinding("HighlightingLexer", binding).contributeTo(
					getLanguage().getEclipsePluginGenModule());
			}
		}
		if (contentAssist) {
			if (this.getProjectConfig().getGenericIde().getRoot() !== null) {
				val binding = new StringConcatenationClient() {
					protected override void appendTo(TargetStringConcatenation target) {
						target.append(
							"binder.bind(org.eclipse.xtext.ide.editor.contentassist.antlr.internal.Lexer.class)" +
								".annotatedWith(com.google.inject.name.Names.named(" +
								"org.eclipse.xtext.ide.LexerIdeBindings.CONTENT_ASSIST" + ")).to(" + lexerGrammar +
								".class);");
					}
				};
				new GuiceModuleAccess.BindingFactory().addConfiguredBinding("ContentAssistLexer", binding).contributeTo(
					getLanguage().getIdeGenModule());

			}
			if (this.getProjectConfig().getEclipsePlugin().getRoot() !== null) {
				val binding = new StringConcatenationClient() {
					protected override void appendTo(TargetStringConcatenation target) {
						target.append(
							"binder.bind(org.eclipse.xtext.ide.editor.contentassist.antlr.internal.Lexer.class)" +
								".annotatedWith(com.google.inject.name.Names.named(" +
								"org.eclipse.xtext.ide.LexerIdeBindings.CONTENT_ASSIST" + ")).to(" + lexerGrammar +
								".class);");
					}
				};
				new GuiceModuleAccess.BindingFactory().addConfiguredBinding("ContentAssistLexer", binding).contributeTo(
					getLanguage().getEclipsePluginGenModule());
			}
		}

		var srcGenFsa = this.getProjectConfig().getRuntime().getSrcGen();
		if (contentAssist || highlighting) {
			if (this.getProjectConfig().getGenericIde().getRoot() !== null) {
				srcGenFsa = this.getProjectConfig().getGenericIde().getSrcGen();
			} else {
				srcGenFsa = this.getProjectConfig().getEclipsePlugin().getSrcGen();
			}
		}
		val srcGenPath = srcGenFsa.getPath();
		val grammarFile = srcGenPath + "/" + getLexerGrammar().replace('.', '/') + ".g";
		var generateTo = "";
		if (getLexerGrammar().lastIndexOf('.') != -1) {
			generateTo = getLexerGrammar().substring(0, getLexerGrammar().lastIndexOf('.'));
		}
		generateTo = srcGenPath + "/" + generateTo.replace('.', '/');
		addAntlrParam("-fo");
		addAntlrParam(generateTo);
		val encoding = getCodeConfig().getEncoding();
		getAntlrTool().runWithEncodingAndParams(grammarFile, encoding, getAntlrParams());

		val lexerType = new TypeReference(getLexerGrammar());
		splitParserAndLexerIfEnabled(srcGenFsa, null /* parser */ , lexerType);
		normalizeTokens(srcGenFsa, getLexerGrammar().replace('.', '/') + ".tokens");
		suppressWarnings(srcGenFsa, lexerType);
		normalizeLineDelimiters(srcGenFsa, lexerType);
	}

	def create it: <AbstractRule, Integer>newHashMap() getRule2Index(Grammar grammar) {
		var i = -1
		for (rule : grammar.allRules)
			put(rule, i = i + 1)
	}

	def getStateTokens(Grammar grammar, ILexerStatesProvider.ILexerStates nfa) {
		val groups = <Pair<Object, ILexerStatesProvider.ILexerState>, Token>newLinkedHashMap()
		for (s : nfa.allStates) {
			for (t : s.tokens) {
				var x = groups.get(t -> null)
				if (x === null)
					groups.put(t -> null, x = new Token(newLinkedHashSet(), t, null))
				x.sources.add(s)
			}
			for (t : nfa.getOutgoingTransitions(s)) {
				var x = groups.get(t.token -> t.target)
				if (x === null)
					groups.put(t.token -> t.target, x = new Token(newLinkedHashSet(), t.token, t.target))
				x.sources.add(s)
			}
		}
		val rule2index = grammar.rule2Index
		groups.values.sortBy[switch (it.token) { AbstractRule: rule2index.get(it.token) String: 0 }]
	}

	def getStatelessTerminalRules(Grammar grammar, ILexerStatesProvider.ILexerStates nfa) {
		val stateful = nfa.allStates.map[tokens + outgoingTransitions.map[token]].flatten.filter(typeof(TerminalRule)).
			toSet
		grammar.allTerminalRules.filter[!stateful.contains(it)]
	}

	def genLexer(Grammar grammar, ILexerStatesProvider.ILexerStates nfa) '''
		lexer grammar «lexerGrammar.lastToken(".")»;
		
		options {
			tokenVocab=Internal«grammar.name.lastToken(".") + "Lexer"»;
		}
		
		@header {
		package «lexerGrammar.skipLastToken(".")»;
		
		// Use our own Lexer superclass by means of import.
		«IF contentAssist»
			import org.eclipse.xpect.ide.services.Lexer;
		«ELSE» 
			import org.eclipse.xpect.services.Lexer;
		«ENDIF»
		}
		
		@members{
			«FOR s : nfa.allStates»
				// state «s.name» = «s.ID»
			«ENDFOR»
			private int tokenstate = «nfa.start.ID»;
		}
		
		«FOR s : getStateTokens(grammar, nfa)»
			«genToken(grammar, s.sources, s.token, s.target)»
		«ENDFOR»
		
		«FOR rule : getStatelessTerminalRules(grammar, nfa)»
			RULE_«rule.name»: «rule.toLexerBody»;
		«ENDFOR»
	'''

	def int toBitmask(Set<ILexerStatesProvider.ILexerState> sources) {
		sources.map[ID].reduce[Integer a, Integer b|a + b]
	}

	def guardAction(Set<ILexerStatesProvider.ILexerState> sources) {
		"{(tokenstate & " + sources.toBitmask + ") != 0}?=>"
	}

	def guardAction(Set<ILexerStatesProvider.ILexerState> sources, String keyword) {
		"{(tokenstate & " + sources.toBitmask + ") != 0 && matches(\"" + keyword + "\")}?=>"
	}

	def transitionAction(ILexerStatesProvider.ILexerState target) {
		"{tokenstate=" + target.ID + ";}"
	}

	def dispatch genToken(Grammar grammar, Set<ILexerStatesProvider.ILexerState> sources, String keyword, Void NULL) '''
		«val keywords = KeywordHelper::getHelper(grammar)»
		«keywords.getRuleName(keyword)»: «sources.guardAction(keyword)» '«AntlrGrammarGenUtil::toAntlrString(keyword)»';
	'''

	def dispatch genToken(Grammar grammar, Set<ILexerStatesProvider.ILexerState> sources, TerminalRule rule,
		Void NULL) '''
		RULE_«rule.name»: «if("ANY_OTHER" != rule.name) sources.guardAction» «rule.toLexerBody»;
	'''

	def dispatch genToken(Grammar grammar, Set<ILexerStatesProvider.ILexerState> sources, String keyword,
		ILexerStatesProvider.ILexerState target) '''
		«val keywords = KeywordHelper::getHelper(grammar)»
		«keywords.getRuleName(keyword)»: «sources.guardAction(keyword)» '«AntlrGrammarGenUtil::toAntlrString(keyword)»' «target.
			transitionAction»;
	'''

	def dispatch genToken(Grammar grammar, Set<ILexerStatesProvider.ILexerState> sources, TerminalRule rule,
		ILexerStatesProvider.ILexerState target) '''
		RULE_«rule.name»: «if("ANY_OTHER" != rule.name) sources.guardAction» «rule.toLexerBody»  «target.transitionAction»;
	'''
}

@Data class TokenNew {
	Set<ILexerStatesProvider.ILexerState> sources
	Object token
	ILexerStatesProvider.ILexerState target
}
