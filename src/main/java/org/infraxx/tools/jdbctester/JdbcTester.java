package org.infraxx.tools.jdbctester;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class JdbcTester implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcTester.class);

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Starting ...");
        CommandLine cmd = parseArgs(args);
        String host = cmd.getOptionValue(CliOptions.HOST.getOpt());
        String database = cmd.getOptionValue(CliOptions.DATABASE.getOpt());
        String user = cmd.getOptionValue(CliOptions.USER.getOpt());
        String password = cmd.getOptionValue(CliOptions.PASSWORD.getOpt());
        String query = cmd.getOptionValue(CliOptions.QUERY.getOpt());

        final boolean active = testMysqlDatabase(host, database, user, password, query);
        if (active) {
            LOG.info("{}/{} - SUCCESS", host, database);
        } else {
            LOG.error("{}/{} - FAILURE", host, database);
        }
    }

    private boolean testMysqlDatabase(String host, String database, String user, String password, String query) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setServerName(host);
        dataSource.setDatabaseName(database);
        try {
            dataSource.setServerTimezone("UTC");
        } catch (SQLException e) {
            LOG.warn("Can't set server timezone to UTC");
        }

        try(Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement();) {
            stmt.execute(query);
            return true;
        } catch (SQLException e) {
            LOG.error(e.toString());
            return false;
        } 
    }

    private static void printCliHelp(Options options) {
        new HelpFormatter().printHelp("jdbc-tester", options);
    }

    private static CommandLine parseArgs(String[] args) {
        Options options = buildCliOptions();
        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            if (cmd.getOptions().length == 0 || cmd.hasOption(CliOptions.HELP.getOpt())) {
                printCliHelp(options);
            }
            return cmd;
        } catch (ParseException e) {
            LOG.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    private static Options buildCliOptions() {
        Options options = new Options();
        options.addOption(CliOptions.HOST);
        options.addOption(CliOptions.DATABASE);
        options.addOption(CliOptions.USER);
        options.addOption(CliOptions.PASSWORD);
        options.addOption(CliOptions.QUERY);
        options.addOption(CliOptions.HELP);
        return options;
    }
}
