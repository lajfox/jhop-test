package com.techstar.modules.flexpaper.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ProcessUtils {

	private static final Logger logger = LoggerFactory.getLogger(ProcessUtils.class);

	public static boolean exec(String execString) {
		boolean result = true;

		logger.info("Executing start:{}", execString);

		List<String> commands = commandLineAsList(execString);

		ProcessBuilder pb = new ProcessBuilder(commands);
		pb.redirectErrorStream(true);
		try {
			Process p = pb.start();

			BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;
			while ((line = is.readLine()) != null) {
				if (line.toLowerCase().startsWith("warning")) {
					logger.warn("WARN:{}", line);
				} else if (line.toLowerCase().startsWith("error")) {
					logger.error("ERROR:{} ", line);
					result = false;
				} else if (line.toLowerCase().startsWith("fatal")) {
					logger.error("FATAL:{} ", line);
					result = false;
				} else {
					logger.info(line);
				}
			}
			try {
				p.waitFor();
			} catch (InterruptedException e) {
				logger.error("{}", e);
				return false;
			}
		} catch (IOException e) {
			logger.error("{}", e);
			return false;
		}

		logger.info("Executing end:{}", execString);

		return result;
	}

	public static String execs(String execString) {

		logger.info("Executing start:{}", execString);

		String ret = "";

		List<String> commands = commandLineAsList(execString);

		ProcessBuilder pb = new ProcessBuilder(commands);
		pb.redirectErrorStream(true);

		try {
			Process p = pb.start();

			BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = is.readLine()) != null) {
				ret += "	" + line;
			}
			try {
				p.waitFor();
			} catch (InterruptedException e) {
				logger.error("{}", e);
				return null;
			}
		} catch (IOException e) {
			logger.error("{}", e);
			return null;
		}

		logger.info("Executing end:{}", execString);

		return ret;
	}

	private static List<String> commandLineAsList(String commandLine) {
		List<String> commands = new ArrayList<String>();
		String elt = "";
		boolean insideString = false;

		for (int i = 0; i < commandLine.length(); i++) {
			char c = commandLine.charAt(i);

			if (!insideString && (c == ' ' || c == '\t')) {
				if (elt.length() > 0) {
					commands.add(elt);
					elt = "";
				}
				continue;
			} else if (c == '"') {
				insideString = !insideString;
			}

			if (c != '"') {
				elt += c;
			}
		}
		if (elt.length() > 0) {
			commands.add(elt);
		}

		return commands;
	}
}
