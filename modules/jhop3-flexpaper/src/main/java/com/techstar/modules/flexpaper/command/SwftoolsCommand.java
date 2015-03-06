package com.techstar.modules.flexpaper.command;

import java.io.IOException;

import com.techstar.modules.flexpaper.domain.QueryParam;

public interface SwftoolsCommand {

	void execute(final CommandContext context) throws IOException;

	boolean execute(final QueryParam param, final boolean upload) throws IOException;

}
