package org.infraxx.tools.jdbctester;

import org.apache.commons.cli.Option;

public class CliOptions {

    public static final Option HOST = Option.builder("h")
            .required(true)
            .desc("MySQL host.")
            .hasArg()
            .argName("MySQL host")
            .build();

    public static final Option USER = Option.builder("u")
            .desc("MySQL user.")
            .required(true)
            .hasArg()
            .argName("MySQL user")
            .build();

    public static final Option PASSWORD = Option.builder("p")
            .desc("MySQL password.")
            .required(true)
            .hasArg()
            .argName("MySQL password")
            .build();

    public static final Option DATABASE = Option.builder("d")
            .desc("MySQK database.")
            .required(true)
            .hasArg()
            .argName("MySQL database")
            .build();

    public static final Option QUERY = Option.builder("e")
            .desc("MySQL query.")
            .required(true)
            .hasArg()
            .argName("MySQL query")
            .build();

    public static final Option HELP = Option.builder("?")
            .longOpt("help")
            .desc("Show help message")
            .hasArg(false)
            .build();
}
