package de.marcelgross.httphero;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Link {
	private String url;

	private String mediaType;

	private String relationType;

	public Link() {
	}

	public Link(final String url, final String mediaType, final String relationType) {
		this.url = url;
		this.mediaType = mediaType;
		this.relationType = relationType;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public String getMediaType() {
		return this.mediaType;
	}

	public void setMediaType(final String mediaType) {
		this.mediaType = mediaType;
	}

	public String getRelationType() {
		return this.relationType;
	}

	public void setRelationType(final String relationType) {
		this.relationType = relationType;
	}

	@Override
	public String toString() {
		return "Link{" +
				"url='" + this.url + '\'' +
				", mediaType='" + this.mediaType + '\'' +
				", relationType='" + this.relationType + '\'' +
				'}';
	}
	public static Link parseFromHttpHeader(final String header) {
		final String[] elements = header.split(";");
		if (elements.length == 3)
			return new Link(parseHref(elements[0]), parseType(elements[2]), parseRel(elements[1]));
		return new Link(parseHref(elements[0]), null, parseRel(elements[1]));
	}

	private static String parseHref(final String headerElement) {
		return parse(headerElement, "<([^>]*)>");
	}

	private static String parseRel(final String headerElement) {
		return parse(headerElement, "^rel=\"(.+)\"$");
	}

	private static String parseType(final String headerElement) {
		return parse(headerElement, "^type=\"(.+)\"$");
	}

	private static String parse(final String headerElement, final String patternExpression) {
		final Pattern pattern = Pattern.compile(patternExpression);
		final Matcher matcher = pattern.matcher(headerElement);
		return matcher.find() ? matcher.group(1) : null;
	}
}