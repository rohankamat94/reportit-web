package com.cirs.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;

public final class CIRSConstants {
	private CIRSConstants() {
	}

	public static final String IMAGE_ROOT = "C:\\CIRSServer\\images\\root";
	private static final String USER_IMAGE = IMAGE_ROOT + "\\users\\";

	public static Path getUserImageDir() {
		Path p = Paths.get(USER_IMAGE);
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
