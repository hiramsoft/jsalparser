package com.hiramsoft.commons.jsalparser;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Created by ihiram on 8/27/14.
 */
public class CloudFrontLogTest extends TestCase
{
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public CloudFrontLogTest(String testName)
	{
		super( testName );
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite()
	{
		return new TestSuite( CloudFrontLogTest.class );
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testHappyPath() throws IOException
	{
		URL gzipUrl = this.getClass().getResource("/E2EXAMPLE111.2014-08-28-04.AnTlR3xmple.gz");
		File gzipFile = new File(gzipUrl.getFile());
		FileInputStream gzipFileStream = new FileInputStream(gzipFile);

		GZIPInputStream gzipInputStream = new GZIPInputStream(gzipFileStream);

		// The parser takes any InputStream, including a GZIPInputStream
		List<CloudFrontWebLogEntry> entries = JSalParser.parseCloudFrontLog(gzipInputStream);

		assertEquals(6, entries.size());
		CloudFrontWebLogEntry entry = entries.get(0);
		assertEquals(entry.getDateTime(), OffsetDateTime.of(2014, 8, 28, 04, 48, 38, 0, ZoneOffset.UTC));
		assertEquals(entry.getxEdgeLocation(), "SEA50");
		assertEquals(entry.getServerToClientBytes(), 19740);
		assertEquals(entry.getClientIpAddress(), "192.168.0.1");
		assertEquals(entry.getClientToServerMethod(), "GET");
		assertEquals(entry.getClientToServerHost(), "example.cloudfront.net");
		assertEquals(entry.getClientToServerUriStem(), "/media/example.jpg");
		assertEquals(entry.getServerToClientStatus(), 200);
		assertEquals(entry.getClientToServerReferrer(), "http://example.cloudfront.net/index.html");
		assertEquals(entry.getClientToServerUserAgent(), "Mozilla/5.0%2520(Macintosh;%2520Intel%2520Mac%2520OS%2520X%252010.9;%2520rv:31.0)%2520Gecko/20100101%2520Firefox/31.0");
		assertEquals(entry.getClientToServerQueryString(), null);
		assertEquals(entry.getClientToServerCookies(), null);
		assertEquals(entry.getxEdgeResultType(), "Miss");
		assertEquals(entry.getxEdgeRequestId(), "UJZErcBpEDqVGccZsEOfXDdIU7E1M5GyQBJMBKAUk4kr75QWLUKX8g==");
		assertEquals(entry.getxHostHeader(), "example.cloudfront.net");
		assertEquals(entry.isClientToServerIsHttps(), false);
		assertEquals(entry.getClientToServerBytes(), 414);
		assertEquals(entry.getTimeTakenMilliSeconds(), 43);
		assertEquals(entry.getExtras().size(), 0);

		gzipInputStream.close();
	}

	public void testOutOfOrder() throws IOException
	{
		URL outOfOrderUrl = this.getClass().getResource("/cloudfront-out-of-order.log");
		File outOfOrderFile = new File(outOfOrderUrl.getFile());
		FileInputStream outOfOrderStream = new FileInputStream(outOfOrderFile);

		List<CloudFrontWebLogEntry> entries = JSalParser.parseCloudFrontLog(outOfOrderStream);

		assertEquals(1, entries.size());
		CloudFrontWebLogEntry entry = entries.get(0);
		assertEquals(entry.getDateTime(), OffsetDateTime.of(2014, 8, 28, 04, 48, 38, 0, ZoneOffset.UTC));
		assertEquals(entry.getxEdgeLocation(), "SEA50");
		assertEquals(entry.getServerToClientBytes(), 19740);
		assertEquals(entry.getClientIpAddress(), "192.168.0.1");
		assertEquals(entry.getClientToServerMethod(), "GET");
		assertEquals(entry.getClientToServerHost(), "example.cloudfront.net");
		assertEquals(entry.getClientToServerUriStem(), "/media/example.jpg");
		assertEquals(entry.getServerToClientStatus(), 200);
		assertEquals(entry.getClientToServerReferrer(), "http://example.cloudfront.net/index.html");
		assertEquals(entry.getClientToServerUserAgent(), "Mozilla/5.0%2520(Macintosh;%2520Intel%2520Mac%2520OS%2520X%252010.9;%2520rv:31.0)%2520Gecko/20100101%2520Firefox/31.0");
		assertEquals(entry.getClientToServerQueryString(), null);
		assertEquals(entry.getClientToServerCookies(), null);
		assertEquals(entry.getxEdgeResultType(), "Miss");
		assertEquals(entry.getxEdgeRequestId(), "UJZErcBpEDqVGccZsEOfXDdIU7E1M5GyQBJMBKAUk4kr75QWLUKX8g==");
		assertEquals(entry.getxHostHeader(), "example.cloudfront.net");
		assertEquals(entry.isClientToServerIsHttps(), false);

		// this file is missing two headers (i.e. Amazon has come out with a new version of the protocol)
		// We should still capture them
		assertEquals(entry.getExtras().size(), 2);

		// However, without header info we only get the raw strings
		assertEquals(entry.getExtras().get(0), "414");
		assertEquals(entry.getExtras().get(1), "0.043");

		outOfOrderStream.close();
	}

	public void testSynopsis() throws IOException
	{
		URL gzipUrl = this.getClass().getResource("/E2EXAMPLE111.2014-08-28-04.AnTlR3xmple.gz");
		File gzipFile = new File(gzipUrl.getFile());
		FileInputStream gzipFileStream = new FileInputStream(gzipFile);
		// Alternatively you could use the AWS SDK
		// At this point you just need some kind of InputStream

		java.util.zip.GZIPInputStream gzipInputStream = new java.util.zip.GZIPInputStream(gzipFileStream);

		// Process records inline by passing a visitor to effectively get "streaming" log processing
		// The only two things you need are an InputStream and a visitor
		// JSalParser is Thread-Safe
		JSalParser.parseCloudFrontLog(gzipInputStream ,new ICloudFrontLogVisitor() {
			int count = 0;
			@Override
			public void accept(CloudFrontWebLogEntry entry) {
				System.out.print("Processing entry #" + (count++) + " from " + entry.getDateTime() + " ");
				// Date is returned as a JODA DateTime object.

				// Numbers are surfaced as Ints and Longs
				if(entry.getServerToClientStatus() == 200)
				{
					System.out.println("OK");
				}
				else
				{
					System.out.println("NOT_OK");
				}

				// You will get:
				/***********
				 Processing entry #0 from 2014-08-28T04:48:38.000Z OK
				 Processing entry #1 from 2014-08-28T04:48:38.000Z OK
				 Processing entry #2 from 2014-08-28T04:49:23.000Z NOT_OK
				 Processing entry #3 from 2014-08-28T04:48:37.000Z OK
				 Processing entry #4 from 2014-08-28T04:48:38.000Z NOT_OK
				 Processing entry #5 from 2014-08-28T04:48:38.000Z OK
				 ***********/
			}
		});

		gzipInputStream.close();
	}
}
