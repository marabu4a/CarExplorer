package com.example.carexplorer.util

object HTMLUtil {

    private const val FONT_FAMILY = "Futura"
    private const val FONT_FILE_NAME = "fonts/FuturaPT_Medium.woff"

    fun getHtmlWithNewFont(html: String): String {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<style>" +
                "img{display: inline;height: auto;max-width: 100%;}" +
                "@font-face {" +
                "font-family: " + FONT_FAMILY + ";" +
                "src: url('" + FONT_FILE_NAME + "');" +
                "}" +
                "* {font-family: '" + FONT_FAMILY + "' !important;}" +
                "* {font-size: 1rem !important;}" +
                "</style>" +
                "</head>\n" +
                "<body>\n" +
                html +
                "\n" +
                "</body>\n" +
                "</html>";
    }

}