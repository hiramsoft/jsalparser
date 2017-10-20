package com.hiramsoft.commons.jsalparser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by ihiram on 8/27/14.
 */
public class JSalParser {

    ////////
    // S3
    ///////

    public static List<S3LogEntry> parseS3Log(String content) {
        CharStream inputStream = CharStreams.fromString(content);
        return parseS3Log(inputStream);
    }

    public static List<S3LogEntry> parseS3Log(InputStream content) throws IOException {
        CharStream inputStream = CharStreams.fromStream(content);
        return parseS3Log(inputStream);
    }

    private static List<S3LogEntry> parseS3Log(CharStream content) {
        ListVisitor listVisitor = new ListVisitor();
        parseS3Log(content, listVisitor);
        return listVisitor.getS3();
    }

    ////////
    // S3 Visitor-based methods
    ///////

    public static void parseS3Log(InputStream content, IS3LogVisitor visitor) throws IOException {
        CharStream inputStream = CharStreams.fromStream(content);
        parseS3Log(inputStream, visitor);
    }

    private static void parseS3Log(CharStream content, IS3LogVisitor visitor) {
        S3TypedLogGrammarLexer lexer = new S3TypedLogGrammarLexer(content);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        S3TypedLogGrammarParser parser = new S3TypedLogGrammarParser(tokens);
        parser.file(visitor);
    }


    ///////
    // CloudFront
    //////

    public static List<CloudFrontWebLogEntry> parseCloudFrontLog(String content) {
        CharStream inputStream = CharStreams.fromString(content);
        return parseCloudFrontLog(inputStream);
    }

    public static List<CloudFrontWebLogEntry> parseCloudFrontLog(InputStream content) throws IOException {
        CharStream inputStream = CharStreams.fromStream(content);

        return parseCloudFrontLog(inputStream);
    }

    private static List<CloudFrontWebLogEntry> parseCloudFrontLog(CharStream content) {

        ListVisitor listVisitor = new ListVisitor();
        parseCloudFrontLog(content, listVisitor);
        return listVisitor.getCf();
    }

    //////////
    // CloudFront Visitor-based methods
    //////////

    public static void parseCloudFrontLog(InputStream content, ICloudFrontLogVisitor visitor) throws IOException {
        CharStream inputStream = CharStreams.fromStream(content);

        parseCloudFrontLog(inputStream, visitor);
    }

    private static void parseCloudFrontLog(CharStream content, ICloudFrontLogVisitor visitor) {
        CloudFrontLogGrammarLexer lexer = new CloudFrontLogGrammarLexer(content);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CloudFrontLogGrammarParser parser = new CloudFrontLogGrammarParser(tokens);

        parser.file(visitor);
    }
}
