package teammates.common.util;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.appengine.api.datastore.Text;

/**
 * Class contains methods to sanitize user provided
 * parameters so that they conform to our data format
 * and possible threats can be removed first.
 */
public final class Sanitizer {
    
    private Sanitizer() {
        // utility class
    }
    
    /**
     * Sanitizes a google ID by removing leading/trailing whitespace
     * and the trailing "@gmail.com".
     * 
     * @param rawGoogleId
     * @return the sanitized google ID or null (if the parameter was null).
     */
    public static String sanitizeGoogleId(String rawGoogleId) {
        if (rawGoogleId == null) {
            return null;
        }
        
        String sanitized = rawGoogleId.trim();
        if (sanitized.toLowerCase().endsWith("@gmail.com")) {
            sanitized = sanitized.split("@")[0];
        }
        return sanitized.trim();
    }
    
    /**
     * Sanitizes an email address by removing leading/trailing whitespace.
     * 
     * @param rawEmail
     * @return the sanitized email address or null (if the parameter was null).
     */
    public static String sanitizeEmail(String rawEmail) {
        return trimIfNotNull(rawEmail);
    }
    
    /**
     * Sanitizes name by removing leading, trailing, and duplicate internal whitespace.
     * 
     * @param rawName
     * @return the sanitized name or null (if the parameter was null).
     */
    public static String sanitizeName(String rawName) {
        return StringHelper.removeExtraSpace(rawName);
    }
    
    /**
     * Sanitizes title by removing leading, trailing, and duplicate internal whitespace.
     * 
     * @param rawTitle
     * @return the sanitized title or null (if the parameter was null).
     */
    public static String sanitizeTitle(String rawTitle) {
        return StringHelper.removeExtraSpace(rawTitle);
    }
    
    /**
     * Sanitizes a user input text field by removing leading/trailing whitespace.
     * i.e. comments, instructions, etc.
     * 
     * @param rawText
     * @return the sanitized text or null (if the parameter was null).
     */
    public static String sanitizeTextField(String rawText) {
        return trimIfNotNull(rawText);
    }
    
    /**
     * Sanitizes a user input text field by removing leading/trailing whitespace.
     * i.e. comments, instructions, etc.
     * 
     * @param rawText
     * @return the sanitized text or null (if the parameter was null).
     */
    public static Text sanitizeTextField(Text rawText) {
        if (rawText == null) {
            return null;
        }
        return new Text(trimIfNotNull(rawText.getValue()));
    }

    /**
     * Escape the string for inserting into javascript code.
     * This automatically calls {@link #sanitizeForHtml} so make it safe for HTML too.
     *
     * @param string
     * @return the sanitized string or null (if the parameter was null).
     */
    public static String sanitizeForJs(String str) {
        if (str == null) {
            return null;
        }
        return Sanitizer.sanitizeForHtml(
                str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("'", "\\'")
                .replace("#", "\\#"));
    }

    /**
     * Sanitizes the string for inserting into HTML. Converts special characters
     * into HTML-safe equivalents.
     */
    public static String sanitizeForHtml(String str) {
        if (str == null) {
            return null;
        }
        return str.replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("/", "&#x2f;")
                .replace("'", "&#39;")
                //To ensure when apply sanitizeForHtml for multiple times, the string's still fine
                //Regex meaning: replace '&' with safe encoding, but not the one that is safe already
                .replaceAll("&(?!(amp;)|(lt;)|(gt;)|(quot;)|(#x2f;)|(#39;))", "&amp;");
    }

    /**
     * Escapes HTML tag safely. This function can be applied multiple times.
     */
    public static String sanitizeForHtmlTag(String str) {
        if (str == null) {
            return null;
        }
        return str.replace("<", "&lt;").replace(">", "&gt;");
    }
    
    /**
     * Sanitizes a list of strings for inserting into HTML.
     */
    public static List<String> sanitizeForHtml(List<String> list) { 
        List<String> sanitizedList = new ArrayList<String>();
        for (String str : list) {
            sanitizedList.add(sanitizeForHtml(str));
        }
        return sanitizedList;
    }
    
    /**
     * Sanitizes a set of strings for inserting into HTML.
     */
    public static Set<String> sanitizeForHtml(Set<String> set) { 
        Set<String> sanitizedSet = new TreeSet<String>();
        for (String str : set) {
            sanitizedSet.add(sanitizeForHtml(str));
        }
        return sanitizedSet;
    }
    
    /**
     * Converts a string to be put in URL (replaces some characters)
     */
    public static String sanitizeForUri(String uri) {
        try {
            return URLEncoder.encode(uri, Const.SystemParams.ENCODING);
        } catch (UnsupportedEncodingException wonthappen) {
            return uri;
        }
    }
    
    /**
     * Sanitizes the given URL for the parameter {@link Const.ParamsNames.NEXT_URL}.
     * The following characters will be sanitized:
     * <ul>
     * <li>&, to prevent the parameters of the next URL from being considered as
     *     part of the original URL</li>
     * <li>%2B (encoded +), to prevent Google from decoding it back to +,
     *     which is used to encode whitespace in some cases</li>
     * <li>%23 (encoded #), to prevent Google from decoding it back to #,
     *     which is used to traverse the HTML document to a certain id</li>
     * </ul>
     *
     * @param url
     * @return the sanitized url or null (if the parameter was null).
     */
    public static String sanitizeForNextUrl(String url) {
        if (url == null) {
            return null;
        }
        return url.replace("&", "${amp}").replace("%2B", "${plus}").replace("%23", "${hash}");
    }
    
    /**
     * Recovers the URL from sanitization due to {@link #sanitizeForNextUrl}.
     * In addition, any un-encoded whitespace (they may be there due to Google's 
     * behind-the-screen decoding process) will be encoded again to +.
     */
    public static String desanitizeFromNextUrl(String url) {
        return url.replace("${amp}", "&").replace("${plus}", "%2B").replace("${hash}", "%23")
                  .replace(" ", "+");
    }
    
    public static String sanitizeForRichText(String richText) {
        if (richText == null) {
            return null;
        }
        return escapeHtml4(richText);
    }
    
    /**
     * Sanitize the string for searching. 
     */
    public static String sanitizeForSearch(String str) {
        if (str == null) {
            return null;
        }
        return str
                //general case for punctuation
                .replace("`", " ").replace("!", " ").replace("#", " ").replace("$", " ").replace("%", " ").replace("^", " ")
                .replace("&", " ").replace("[", " ").replace("]", " ").replace("{", " ").replace("}", " ").replace("|", " ")
                .replace(";", " ").replace("*", " ").replace(".", " ").replace("?", " ").replace("'", " ").replace("/", " ")
                //to prevent injection
                .replace("=", " ")
                .replace(":", " ")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
    
    /**
     * Sanitizes the string for comma-separated values (CSV) file output.<br>
     * We follow the definition described by RFC 4180:<br>
     * {@link http://tools.ietf.org/html/rfc4180}
     */
    public static String sanitizeForCsv(String str) {
        return "\"" + str.replace("\"", "\"\"") + "\"";
    }
    
    /**
     * Sanitizes the list of strings for comma-separated values (CSV) file output.<br>
     * We follow the definition described by RFC 4180:<br>
     * {@link http://tools.ietf.org/html/rfc4180}
     */
    public static List<String> sanitizeListForCsv(List<String> strList) {
        List<String> sanitizedStrList = new ArrayList<String>();
        
        Iterator<String> itr = strList.iterator();
        while (itr.hasNext()) {
            sanitizedStrList.add(sanitizeForCsv(itr.next()));
        }
        
        return sanitizedStrList;
    }

    /**
     * Trims the string if it is not null. 
     * 
     * @param string
     * @return the trimmed string or null (if the parameter was null).
     */
    private static String trimIfNotNull(String string) {
        return (string == null) ? null : string.trim();
    }
    
    /**
     * Convert the string to a safer version for XPath
     * For example:
     * Will o' The Wisp => concat('Will o' , "'" , ' The Wisp' , '')
     * This will result in the same string when read by XPath.
     * 
     * This is used when writing the test case for some special characters
     * such as ' and "
     * 
     * @param text
     * @return safer version of the text for XPath
     */
    public static String convertStringForXPath(String text) {
        StringBuilder result = new StringBuilder();
        int startPos = 0;
        int i = 0;
        while (i < text.length()) {
            while (i < text.length() && text.charAt(i) != '\'') {
                i++;
            }
            if (startPos < i) {
                result.append('\'').append(text.substring(startPos, i)).append("',");
                startPos = i;
            }
            while (i < text.length() && text.charAt(i) == '\'') {
                i++;
            }
            if (startPos < i) {
                result.append('\"').append(text.substring(startPos, i)).append("\",");
                startPos = i;
            }
        }
        if (result.length() == 0) {
            return "''";
        }
        return "concat(" + result.toString() + "'')";
    }
}
