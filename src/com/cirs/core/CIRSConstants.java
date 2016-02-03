package com.cirs.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class CIRSConstants {
	private CIRSConstants() {
	}

	public static final String IMAGE_ROOT = "C:\\CIRSServer\\images\\root";
	private static final String USER_IMAGE = IMAGE_ROOT + "\\users\\";
	private static final String COMPLAINT_IMAGE = IMAGE_ROOT + "\\complaints\\";

	public static enum ImageDir {
		USER(USER_IMAGE), COMPLAINT(COMPLAINT_IMAGE);
		private String assocDir;

		private ImageDir(String assocDir) {
			this.assocDir = assocDir;
		}

		public String getAssocDir() {
			return assocDir;
		}
	}

	public static Path getImageDir(ImageDir dir) {
		Path p = Paths.get(dir.getAssocDir());
		if (!Files.exists(p, LinkOption.NOFOLLOW_LINKS)) {
			try {
				Files.createDirectories(p);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return p;
	}
}
