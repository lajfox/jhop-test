package com.techstar.modules.mail.javamail;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.util.Assert;

public class MimeMessageHelper extends org.springframework.mail.javamail.MimeMessageHelper {

	public MimeMessageHelper(MimeMessage mimeMessage) {
		super(mimeMessage);
	}

	public MimeMessageHelper(MimeMessage mimeMessage, String encoding) {
		super(mimeMessage, encoding);
	}

	public MimeMessageHelper(MimeMessage mimeMessage, boolean multipart) throws MessagingException {
		super(mimeMessage, multipart);
	}

	public MimeMessageHelper(MimeMessage mimeMessage, boolean multipart, String encoding) throws MessagingException {
		super(mimeMessage, multipart, encoding);
	}

	public MimeMessageHelper(MimeMessage mimeMessage, int multipartMode) throws MessagingException {
		super(mimeMessage, multipartMode);
	}

	public MimeMessageHelper(MimeMessage mimeMessage, int multipartMode, String encoding) throws MessagingException {
		super(mimeMessage, multipartMode, encoding);
	}

	/**
	 * Add an attachment to the MimeMessage, taking the content from an
	 * {@code org.springframework.core.io.InputStreamResource}.
	 * <p>
	 * The content type will be determined by the given filename for the
	 * attachment. Thus, any content source will be fine, including temporary
	 * files with arbitrary filenames.
	 * <p>
	 * Note that the InputStream returned by the InputStreamSource
	 * implementation needs to be a <i>fresh one on each call</i>, as JavaMail
	 * will invoke {@code getInputStream()} multiple times.
	 * 
	 * @param attachmentFilename
	 *            the name of the attachment as it will appear in the mail
	 * @param inputStream
	 *            InputStream
	 * @throws MessagingException
	 *             in case of errors
	 * @see #addAttachment(String, java.io.File)
	 * @see #addAttachment(String, javax.activation.DataSource)
	 * @see org.springframework.core.io.Resource
	 */
	public void addAttachment(String attachmentFilename, InputStream inputStream) throws MessagingException {
		String contentType = getFileTypeMap().getContentType(attachmentFilename);
		addAttachment(attachmentFilename, inputStream, contentType);
	}

	/**
	 * Add an attachment to the MimeMessage, taking the content from an
	 * {@code org.springframework.core.io.InputStreamResource}.
	 * <p>
	 * Note that the InputStream returned by the InputStreamSource
	 * implementation needs to be a <i>fresh one on each call</i>, as JavaMail
	 * will invoke {@code getInputStream()} multiple times.
	 * 
	 * @param attachmentFilename
	 *            the name of the attachment as it will appear in the mail
	 * @param inputStream
	 *            InputStream
	 * @param contentType
	 *            the content type to use for the element
	 * @throws MessagingException
	 *             in case of errors
	 * @see #addAttachment(String, java.io.File)
	 * @see #addAttachment(String, javax.activation.DataSource)
	 * 
	 */
	public void addAttachment(String attachmentFilename, InputStream inputStream, String contentType)
			throws MessagingException {

		Assert.notNull(inputStream, "inputStream must not be null");

		DataSource dataSource = createDataSource(inputStream, contentType, attachmentFilename);
		addAttachment(attachmentFilename, dataSource);
	}

	/**
	 * Create an Activation Framework DataSource for the given
	 * InputStreamSource.
	 * 
	 * @param inputStream
	 *            InputStream
	 * @param contentType
	 *            the content type
	 * @param name
	 *            the name of the DataSource
	 * @return the Activation Framework DataSource
	 */
	protected DataSource createDataSource(final InputStream inputStream, final String contentType, final String name) {

		return new DataSource() {
			public InputStream getInputStream() throws IOException {
				return inputStream;
			}

			public OutputStream getOutputStream() {
				throw new UnsupportedOperationException("Read-only javax.activation.DataSource");
			}

			public String getContentType() {
				return contentType;
			}

			public String getName() {
				return name;
			}
		};
	}

}
