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
import java.util.Iterator;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class S3LogTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public S3LogTest(String testName)
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( S3LogTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testHappyPath() throws IOException
    {

		URL s3Url = this.getClass().getResource("/2014-08-27-21-24-10-E5EXAMPLE553");
		File s3File = new File(s3Url.getFile());
		FileInputStream s3FileStream = new FileInputStream(s3File);

		List<S3LogEntry> entries = JSalParser.parseS3Log(s3FileStream);

		assertEquals(entries.size(), 20);

		Iterator iter = entries.iterator();

		S3LogEntry entry =  (S3LogEntry) iter.next();
		// OR, in the real world you you likely just call entries.get(0);
		// Below the test case ensures we were able to read from every line in the file

		assertEquals(entry.getBucketOwner(), "1f000000000c6c88eb9dd89c000000000b35b0000000a5");
		assertEquals(entry.getBucket(), "www.example.com");
		assertEquals(entry.getTime(), OffsetDateTime.of(2014, 8, 27, 20, 20, 5, 0, ZoneOffset.UTC));
		assertEquals(entry.getRemoteIpAddress(), "192.168.0.1");
		assertEquals(entry.getRequester(), null);
		assertEquals(entry.getRequestId(), "BFE596E2F4D94C8F");
		assertEquals(entry.getOperation(), "WEBSITE.GET.OBJECT");
		assertEquals(entry.getKey(), "media/example.jpg");
		assertEquals(entry.getRequestUri(), "GET /media/example.jpg HTTP/1.1");
		assertEquals(entry.getHttpStatus(), 304);
		assertEquals(entry.getErrorCode(), null);
		assertEquals(entry.getBytesSent(), 0);
		assertEquals(entry.getObjectSize(), 27553);
		assertEquals(entry.getTotalTime(), 202);
		assertEquals(entry.getTurnAroundTime(), 0);
		assertEquals(entry.getReferrer(), "http://www.example.com/page.html");
		assertEquals(entry.getUserAgent(), "Mozilla/5.0 (iPhone; CPU iPhone OS 7_1_2 like Mac OS X) AppleWebKit/537.51.2 (KHTML, like Gecko) Version/7.0 Mobile/11D257 Safari/9537.53");
		assertEquals(entry.getVersionId(), null);



		while(iter.hasNext())
		{
			S3LogEntry iterElem = (S3LogEntry) iter.next();
			assertEquals(iterElem.getBucketOwner(), "1f000000000c6c88eb9dd89c000000000b35b0000000a5");
			assertEquals(iterElem.getBucket(), "www.example.com");
			// the test case is constructed so all subsequent log entries
			// occur before the first
			assertTrue(iterElem.getTime().isBefore(entry.getTime()));
			assertEquals(iterElem.getRequester(), "arn:aws:iam::000000000012:user/example-user");

			assertEquals(iterElem.getExtras().size(), 0);
		}

		assertEquals(entry.getExtras().size(), 0);

		s3FileStream.close();
    }

	public void testSynopsis1() throws IOException
	{
		String content = "1f000000000c6c88eb9dd89c000000000b35b0000000a5 www.example.com [27/Aug/2014:20:20:05 +0000] 192.168.0.1 - BFE596E2F4D94C8F WEBSITE.GET.OBJECT media/example.jpg \"GET /media/example.jpg HTTP/1.1\" 304 - - 27553 202 - \"http://www.example.com/page.html\" \"Mozilla/5.0 (iPhone; CPU iPhone OS 7_1_2 like Mac OS X) AppleWebKit/537.51.2 (KHTML, like Gecko) Version/7.0 Mobile/11D257 Safari/9537.53\" -";
		List<S3LogEntry> entries = JSalParser.parseS3Log(content);

		long TenMegabytes = 10000000L;

		for(int i=0;i<entries.size();i++) {
			S3LogEntry entry = entries.get(i);

			// Notice how the numbers are numbers, no additional parsing needed
			if(entry.getObjectSize() > TenMegabytes)
			{
				System.out.println(entry.getTime());

				// getTime() returns a JODA DateTime object,
				// so Java prints:
				// 2014-08-27T20:20:05.000+00:00
			}
		}

		assertEquals(entries.size(), 1);
	}
}
