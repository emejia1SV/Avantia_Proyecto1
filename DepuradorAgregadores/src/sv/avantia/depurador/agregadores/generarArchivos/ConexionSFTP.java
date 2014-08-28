package sv.avantia.depurador.agregadores.generarArchivos;


import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class ConexionSFTP {

	private String sftpHost = "192.168.0.100";
	private int sftpPort = 21;
	private String sftpUser = "claro";
	private String sftpDir = "/home/claro";

	/**
	 * Metodo el cual servira para guardar el archivo a traves del sftp
	 * obteniendo el arreglo de bytes se castiaran en un FileinputStream para
	 * poderse guardar
	 * 
	 * @author Emejia - Avantia Consultores
	 * @param base64Decode
	 *            ( arreglo de bytes que viene desde Jteller atraves de un
	 *            parametro )
	 * @param fileName
	 *            ( nombre del archivo para ser guardado a traves del sftp )
	 * @return un valor boolean que dira si fue guardado exitosamente o no el
	 *         archivo @throws Exception
	 * 
	 * */
	public boolean moveFileToDir(byte[] base64Decode, String fileName)
			throws Exception {
		boolean returnResult = false;
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;

		try {
			JSch jsch = new JSch();
			session = jsch.getSession(this.sftpUser, this.sftpHost,	this.sftpPort);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;
			channelSftp.cd(this.sftpDir);
			InputStream inputStream = new ByteArrayInputStream(base64Decode);
			FileInputStream fileInputStream = (FileInputStream) inputStream;
			channelSftp.put(fileInputStream, fileName);
			channel.disconnect();
			session.disconnect();
			returnResult = true;
		} catch (Exception exception) {
			throw new Exception("error al guardar en el FTP");
		}
		return returnResult;
	}
}
