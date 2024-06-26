package nsu.mmf.syspro.jdu.command;




import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Command {

    public int depth = 3;
    public int limit = 10;
    public final boolean transition;

    public Path path = Path.of(System.getProperty("user.dir"));

    private final HashMap<File, Long> files = new HashMap<>();

    public Command(boolean transition, Integer depth, Integer limit, Path path) {
        this.transition = transition;
        if (depth != null) {
            this.depth = depth;
        }
        if (limit != null) {
            this.limit = limit;
        }
        if (path != null) {
            this.path = path;
        }
    }


    public String apply(Path path, int currentDepth) throws IOException {
        if (currentDepth == 0) {
            SecondaryFunctions.countFilesSize(path.toFile(), files, transition);
        }
        if (files.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        if (currentDepth < depth) {
            File directory = path.toFile();
            HashMap<Long, List<File>> tmp = new HashMap<>();
            File[] listFiles = directory.listFiles();
            if (listFiles != null) {
                for (File file : listFiles) {
                    if (Files.isSymbolicLink(file.toPath()) && transition) {
                        file = Files.readSymbolicLink(file.toPath()).toFile();
                    }
                    long size = files.get(file);
                    if (tmp.containsKey(size)) {
                        tmp.get(size).add(file);
                    } else {
                        List<File> fileList = new ArrayList<>();
                        fileList.add(file);
                        tmp.put(files.get(file), fileList);
                    }
                }
            }
            List<Long> keys = tmp.keySet().stream().sorted().toList();
            keys = keys.subList((keys.size() > limit) ? keys.size() - limit : 0, keys.size());
            int index = 0;
            int k = 0;
            while (k < limit && index < keys.size()) {
                List<File> fileList = tmp.get(keys.get(index));
                File file = fileList.getFirst();
                if (Files.isSymbolicLink(file.toPath()) && transition) {
                    file = Files.readSymbolicLink(file.toPath()).toFile();
                }
                String size = SecondaryFunctions.fileSizeToString(keys.get(index));
                result.append("\t".repeat(currentDepth)).append("/").append(file.getName()).append(size);
                result.append(apply(file.toPath(), currentDepth + 1));
                if (fileList.size() > 1) {
                    fileList.removeFirst();
                } else {
                    index++;
                }
                k++;
            }
        }
        return String.valueOf(result);
    }

    public String apply() throws IOException {
        return apply(path, 0);
    }


    private static class SecondaryFunctions {

        public static long countFilesSize(@NotNull File directory, HashMap<File, Long> files, boolean transition) throws IOException {

            long length = 0;
            File[] listFiles = directory.listFiles();
            if (listFiles != null) {
                for (File file : listFiles) {
                    if (file.isFile()) {
                        files.put(file, file.length());
                        length += file.length();
                    } else if (Files.isSymbolicLink(file.toPath()) && transition) {
                        try {
                            File newFile = Files.readSymbolicLink(file.toPath()).toFile();
                            files.put(newFile, countFilesSize(newFile, files, transition));
                        } catch (IOException ignored) {
                        }
                    } else {
                        long folderSize = countFilesSize(file, files, transition);
                        files.put(file, folderSize);
                        length += folderSize;
                    }
                }
            }
            return length;
        }

        public static @NotNull String fileSizeToString(long size) {
            double newSize = 0;
            int i;
            for (i = 1; i < 4; i++) {
                if (Math.pow(2, 10 * (i + 1)) > size && size >= Math.pow(2, 10 * i)) {
                    newSize = (double) size / Math.pow(2, 10 * i);
                    break;
                }
            }
            String measurement = switch (i) {
                case 1 -> "KiB";
                case 2 -> "MiB";
                case 3 -> "GiB";
                default -> "B";
            };
            String formattedDouble = String.format("%.1f", newSize).replace(",", ".");
            return "[" + formattedDouble + " " + measurement + "]\n";
        }
    }
}
