/*******************************************************************************
 * Copyright (c) 2024 itemis AG (http://www.itemis.eu) and others.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.xpect.releng.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.xpect.releng.utils.MergeableManifest2.BundleOrPackage;
import org.eclipse.xpect.releng.utils.MergeableManifest2.BundleOrPackageList;

/**
 * @author Sebastian - Initial contribution and API
 * @author Mehmet - Adjusted solution for the needs of xpect
 */
public class BumpVersions {

	public static void main(String[] args) throws Exception {
		try {
			File gitRoot = new File(".").getAbsoluteFile().getParentFile().getParentFile().getParentFile();
			Map<String, String> versionsFromDevBom = new HashMap<>();
			addMissingVersions(versionsFromDevBom);
			String xtextVersion = "2.34.0";
			String xpectVersion = "0.3.0";
			for (File bundleDir : gitRoot.listFiles()) {
				if (bundleDir.isDirectory()) {
					File manifest = new File(bundleDir, "META-INF/MANIFEST.MF");
					if (manifest.exists()) {
						MergeableManifest2 mergable;
						try (FileInputStream is = new FileInputStream(manifest)) {
							mergable = new MergeableManifest2(is);
						}
						String bundleSymName = mergable.getMainAttributes().get("Bundle-SymbolicName").replace(";singleton:=true", "");
						if (!bundleSymName.equals("org.eclipse.xpect.doc") && !bundleSymName.equals("org.eclipse.xpect.doc") && !bundleSymName.equals("org.eclipse.xpect.examples")) {
							mergable.getMainAttributes().put(MergeableManifest2.BUNDLE_VERSION,
									xpectVersion + ".qualifier");
						}
						updateRequiredXtextBundles(mergable, xtextVersion);
						updateImportedXtextPackages(mergable, xtextVersion);
						updateExportedXtextPackages(mergable, xpectVersion);
						updateRequiredBundle(mergable, versionsFromDevBom);

						if (mergable.isModified()) {
							try (FileOutputStream out = new FileOutputStream(manifest)) {
								mergable.write(out);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private static final String LSP4J_VERSION = "[0.23.1,0.24.0)";

	private static void addMissingVersions(Map<String, String> bundleToVersion) {
		bundleToVersion.put("org.hamcrest.core", "2.2.0");
		bundleToVersion.put("org.hamcrest.library", "1.3.0");
		bundleToVersion.put("org.eclipse.lsp4j", LSP4J_VERSION);
		bundleToVersion.put("org.eclipse.lsp4j.jsonrpc", LSP4J_VERSION);

		bundleToVersion.put("org.eclipse.jdt.core", "3.37.0");
		bundleToVersion.put("org.eclipse.jdt.core.manipulation", "1.21.0");
		bundleToVersion.put("org.eclipse.jdt.debug", "3.21.300");
		bundleToVersion.put("org.eclipse.jdt.debug.ui", "3.13.300");
		bundleToVersion.put("org.eclipse.jdt.junit", "3.16.300");
		bundleToVersion.put("org.eclipse.jdt.launching", "3.21.100");
		bundleToVersion.put("org.eclipse.jdt.ui", "3.32.0");

		bundleToVersion.put("org.eclipse.ui", "3.205.100");
		bundleToVersion.put("org.eclipse.ui.console", "3.14.0");
		bundleToVersion.put("org.eclipse.ui.editors", "3.17.200");
		bundleToVersion.put("org.eclipse.ui.forms", "3.13.200");
		bundleToVersion.put("org.eclipse.ui.ide", "3.22.100");
		bundleToVersion.put("org.eclipse.ui.navigator", "3.12.300");
		bundleToVersion.put("org.eclipse.ui.views", "3.12.200");
		bundleToVersion.put("org.eclipse.ui.workbench", "3.131.100");
		bundleToVersion.put("org.eclipse.ui.workbench.texteditor", "3.17.300");

		bundleToVersion.put("org.eclipse.swtbot.eclipse.core", "4.1.0");
		bundleToVersion.put("org.eclipse.swtbot.eclipse.finder", "4.1.0");
		bundleToVersion.put("org.eclipse.swtbot.junit4_x", "4.1.0");
		bundleToVersion.put("org.eclipse.swtbot.swt.finder", "4.1.0");

		bundleToVersion.put("org.apache.ant", "1.10.14");
		bundleToVersion.put("org.eclipse.buildship.core", "3.1.9");

		bundleToVersion.put("org.eclipse.compare", "3.10.0");
		bundleToVersion.put("org.eclipse.e4.core.services", "2.4.300");
		bundleToVersion.put("org.eclipse.e4.ui.css.swt.theme", "0.14.300");

		bundleToVersion.put("jakarta.inject.jakarta.inject-api", "2.0.1");

		bundleToVersion.put("org.eclipse.core.databinding", "1.13.200");
		bundleToVersion.put("org.eclipse.core.databinding.beans", "1.10.200");
		bundleToVersion.put("org.eclipse.core.databinding.property", "1.10.200");

		bundleToVersion.put("org.eclipse.core.expressions", "3.9.300");
		bundleToVersion.put("org.eclipse.core.filesystem", "1.10.300");
		bundleToVersion.put("org.eclipse.core.resources", "3.20.100");
		bundleToVersion.put("org.eclipse.core.runtime", "3.31.0");
		bundleToVersion.put("org.eclipse.debug.core", "3.21.300");
		bundleToVersion.put("org.eclipse.debug.ui", "3.18.300");
		bundleToVersion.put("org.eclipse.draw2d", "3.15.0");
		bundleToVersion.put("org.eclipse.emf.codegen", "2.23.0");
		bundleToVersion.put("org.eclipse.emf.codegen.ecore", "2.37.0");
		bundleToVersion.put("org.eclipse.emf.common", "2.30.0");
		bundleToVersion.put("org.eclipse.emf.common.ui", "2.22.0");
		bundleToVersion.put("org.eclipse.emf.ecore", "2.36.0");
		bundleToVersion.put("org.eclipse.emf.ecore.change", "2.16.0");
		bundleToVersion.put("org.eclipse.emf.ecore.editor", "2.18.0");
		bundleToVersion.put("org.eclipse.emf.ecore.xmi", "2.37.0");
		bundleToVersion.put("org.eclipse.emf.edit", "2.21.0");
		bundleToVersion.put("org.eclipse.emf.edit.ui", "2.23.0");
		bundleToVersion.put("org.eclipse.equinox.common", "3.19.0");
		bundleToVersion.put("org.eclipse.help", "3.10.300");
		bundleToVersion.put("org.eclipse.jface", "3.33.0");
		bundleToVersion.put("org.eclipse.jface.databinding", "1.15.200");
		bundleToVersion.put("org.eclipse.jface.text", "3.25.0");
		bundleToVersion.put("org.eclipse.ltk.core.refactoring", "3.14.300");
		bundleToVersion.put("org.eclipse.ltk.ui.refactoring", "3.13.300");
		bundleToVersion.put("org.eclipse.m2e.core", "2.6.0");
		bundleToVersion.put("org.eclipse.m2e.maven.runtime", "3.9.600");
		bundleToVersion.put("org.eclipse.pde.core", "3.18.0");
		bundleToVersion.put("org.eclipse.pde.ui", "3.15.100");
		bundleToVersion.put("org.eclipse.search", "3.16.100");

		bundleToVersion.put("org.eclipse.team.core", "3.10.300");
		bundleToVersion.put("org.eclipse.team.ui", "3.10.300");
	}

	private static void updateRequiredBundle(MergeableManifest2 mergable, Map<String, String> bundleVersions) {
		String oldBundles = mergable.getMainAttributes().get(MergeableManifest2.REQUIRE_BUNDLE);
		if (oldBundles == null)
			return;
		BundleOrPackageList requiredBundles = BundleOrPackageList.fromInput(oldBundles, mergable.getLineDelimiter(),
				"bundle-version");
		List<String> updatedBundles = new ArrayList<>();
		for (BundleOrPackage requiredBundle : requiredBundles.list()) {
			String bundleName = requiredBundle.getName();
			String newVersion = bundleVersions.get(bundleName);
			if (newVersion != null) {
				updatedBundles.add(bundleName + ";bundle-version=\"" + newVersion + "\"");
			}
		}
		mergable.addRequiredBundles(updatedBundles.toArray(new String[0]), true);
	}

	private static void updateRequiredXtextBundles(MergeableManifest2 mergable, String newVersion) {
		String oldBundles = mergable.getMainAttributes().get(MergeableManifest2.REQUIRE_BUNDLE);
		if (oldBundles == null)
			return;
		BundleOrPackageList requiredBundles = BundleOrPackageList.fromInput(oldBundles, mergable.getLineDelimiter(),
				"bundle-version");
		List<String> updatedBundles = new ArrayList<>();
		for (BundleOrPackage requiredBundle : requiredBundles.list()) {
			String bundleName = requiredBundle.getName();
			if (bundleName.startsWith("org.eclipse.xtext")) {
				if (bundleName.endsWith(".example.domainmodel") || bundleName.endsWith("example.domainmodel.ui")) {
					continue;
				}
				updatedBundles.add(bundleName + ";bundle-version=\"" + newVersion + "\"");
			}
		}
		mergable.addRequiredBundles(updatedBundles.toArray(new String[0]), true);
	}

	private static void updateImportedXtextPackages(MergeableManifest2 mergable, String newVersion) {
		String oldPackages = mergable.getMainAttributes().get(MergeableManifest2.IMPORT_PACKAGE);
		if (oldPackages == null)
			return;
		BundleOrPackageList importedPackages = BundleOrPackageList.fromInput(oldPackages, mergable.getLineDelimiter(),
				"version");
		List<String> updatedPackages = new ArrayList<>();
		for (BundleOrPackage importedPackage : importedPackages.list()) {
			String packageName = importedPackage.getName();
			if (packageName.startsWith("org.eclipse.x")) {
				updatedPackages.add(packageName + ";version=\"" + newVersion + "\"");
			}
		}
		mergable.addImportedPackages(updatedPackages.toArray(new String[0]), true);
	}

	private static void updateExportedXtextPackages(MergeableManifest2 mergable, String newVersion) {
		String oldPackages = mergable.getMainAttributes().get(MergeableManifest2.EXPORT_PACKAGE);
		if (oldPackages == null)
			return;
		BundleOrPackageList exportedPackages = BundleOrPackageList.fromInput(oldPackages, mergable.getLineDelimiter(),
				"version");
		List<String> updatedPackages = new ArrayList<>();
		for (BundleOrPackage exportedPackage : exportedPackages.list()) {
			String packageName = exportedPackage.getName();
			updatedPackages.add(packageName + ";version=\"" + newVersion + "\"");
		}
		mergable.addExportedPackages(updatedPackages.toArray(new String[0]), true);
	}

}