package io.github.pashazz.taskmanager;

import io.github.pashazz.taskmanager.command.Command;
import io.github.pashazz.taskmanager.command.CommandHandler;
import io.github.pashazz.taskmanager.exception.CommandException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.*;
import java.util.regex.Pattern;

public class Utils {
    public static String getWordOrQuotedText(@NonNull final Scanner scanner) {
        String rx = "[^\"\\s]+|\"(\\\\.|[^\\\\\"])*\"";
        String found = scanner.findInLine(rx);
        if (found == null)
            return null;
        if (found.startsWith("\"")) {
            found = found.substring(1, found.length() - 1);
        }
        return found;
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

    public static @NonNull <T extends Printable> T scanIdReturnEntity(final Class<T> type, final CrudRepository<T, Long> repo, final Scanner scanner, final Command command ) throws CommandException {
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

    public static @NonNull <T> Long scanId(final Class<T> type, final CrudRepository<T, Long> repo, final Scanner scanner, final Command command) throws CommandException {
        if (!scanner.hasNextLong()) {
            throw new CommandException(command, "Expected: long; not found");
        }
        Long id = scanner.nextLong();
        if (!repo.existsById(id)) {
            throw new CommandException(command, "No entry of type " + type.getName() +" with id " + id);
        }
        return id;
    }

    public static String iteratorToShortString(Iterator<? extends Printable> iter) {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        while (iter.hasNext()) {
            var next = iter.next();
            sb.append(next.shortToString());
            if (iter.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("]");


        return sb.toString();
    }

    public interface EntryHandler<T> {
        void doWith(T item);
    }

    public interface IdHandler {
        void doWith(Long id);
    }
    public static <T extends Printable> void scanIdAndDoWithEntityWhileExists(final Class<T> type, final CrudRepository<T, Long> repo, final Scanner scanner, final Command command, EntryHandler<T> handler) throws CommandException {
        while(scanner.hasNextLong()) {
            handler.doWith(Utils.scanIdReturnEntity(type, repo, scanner, command));
        }
    }

    /**
     * Short version of toString preventing stack overflows
     * @param sb
     */

}
