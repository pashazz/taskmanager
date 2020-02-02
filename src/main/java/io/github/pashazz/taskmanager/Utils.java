package io.github.pashazz.taskmanager;

import io.github.pashazz.taskmanager.command.Command;
import io.github.pashazz.taskmanager.command.CommandHandler;
import io.github.pashazz.taskmanager.exception.CommandException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Utils {
    public static String getWordOrQuotedText(@NonNull final Scanner scanner) {
        Pattern quoted = Pattern.compile("\".*\"");
        if (scanner.hasNext(quoted)) {
            return scanner.next(quoted);
        } else {
            if (scanner.hasNext())
                return scanner.next();
            else {
                return null;
            }
        }
    }

    public static CommandHandler createMultiHandler(@NonNull final Map<String, CommandHandler> handlers, final Command command) {
        return (in, out ) -> {
            String word = getWordOrQuotedText(in);
            if (handlers.containsKey(word)) {
                handlers.get(word).handle(in, out);
            } else {
                throw new CommandException(command, "No such subcommand: " + word);
            }
        };
    }

    public static Pattern makePatternForHandlerMap(@NonNull Iterator<String> iter) {
        StringBuilder patternBuilder = new StringBuilder();

        while (iter.hasNext()) {
            String next = iter.next();
            if (next == null) {
                continue;
            } else {
                if (patternBuilder.length() > 0) {
                    patternBuilder.append('|');
                }
                patternBuilder.append(next);
            }
        }
        return Pattern.compile(patternBuilder.toString());
    }

    public static @NonNull <T> T scanId(final Class<T> type, final CrudRepository<T, Long> repo, final Scanner scanner, final Command command ) throws CommandException {
        if (!scanner.hasNextLong()) {
            throw new CommandException(command, "Expected: long; not found");
        }
        Long id = scanner.nextLong();
        Optional<T> entry = repo.findById(id);
        if (entry.isEmpty()) {
            throw new CommandException(command, "No entry of type " + type.getName() +" with id " + id);
        }
        return entry.get();
    }

    public interface EntryHandler<T> {
        void doWith(T item);
    }
    public static <T> void scanIdAndDoWhileExists(final Class<T> type, final CrudRepository<T, Long> repo, final Scanner scanner, final Command command, EntryHandler<T> handler) throws CommandException {
        while(scanner.hasNextLong()) {
            handler.doWith(Utils.scanId(type, repo, scanner, command));
        }
    }
}
