package ua.knu.knudev.fileserviceapi.folder;

import lombok.Data;
import ua.knu.knudev.fileserviceapi.subfolder.FileSubfolder;

@Data
public class FileFolderProperties<S extends FileSubfolder> {
    private final FileFolder<S> folder;
    private final S subfolder;

    private FileFolderProperties(FileFolder<S> folder, S subfolder) {
        this.folder = folder;
        this.subfolder = subfolder;
    }

    public static <S extends FileSubfolder> Builder<S> builder(FileFolder<S> folder) {
        return new Builder<>(folder);
    }

    public static class Builder<S extends FileSubfolder> {
        private final FileFolder<S> folder;
        private S subfolder;

        private Builder(FileFolder<S> folder) {
            this.folder = folder;
        }

        public Builder<S> subfolder(S subfolder) {
            this.subfolder = subfolder;
            return this;
        }

        public FileFolderProperties<S> build() {
            return new FileFolderProperties<>(folder, subfolder);
        }
    }
}

