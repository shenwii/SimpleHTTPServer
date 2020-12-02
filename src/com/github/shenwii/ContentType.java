package com.github.shenwii;

import java.util.HashMap;
import java.util.Map;

public class ContentType {
	private final static Map<String, String>CONTENT_MAP = new HashMap<String, String>() {
		private static final long serialVersionUID = -8485322144860410145L;
		{
			put("001", "application/x-001");
			put("323", "text/h323");
			put("907", "drawing/907");
			put("acp", "audio/x-mei-aac");
			put("aif", "audio/aiff");
			put("aiff", "audio/aiff");
			put("asa", "text/asa");
			put("asp", "text/asp");
			put("au", "audio/basic");
			put("awf", "application/vnd.adobe.workflow");
			put("bmp", "application/x-bmp");
			put("c4t", "application/x-c4t");
			put("cal", "application/x-cals");
			put("cdf", "application/x-netcdf");
			put("cel", "application/x-cel");
			put("cg4", "application/x-g4");
			put("cit", "application/x-cit");
			put("cml", "text/xml");
			put("cmx", "application/x-cmx");
			put("crl", "application/pkix-crl");
			put("csi", "application/x-csi");
			put("cut", "application/x-cut");
			put("dbm", "application/x-dbm");
			put("dcd", "text/xml");
			put("der", "application/x-x509-ca-cert");
			put("dib", "application/x-dib");
			put("doc", "application/msword");
			put("drw", "application/x-drw");
			put("dwg", "application/x-dwg");
			put("dxf", "application/x-dxf");
			put("emf", "application/x-emf");
			put("ent", "text/xml");
			put("eps", "application/x-ps");
			put("etd", "application/x-ebx");
			put("fax", "image/fax");
			put("fif", "application/fractals");
			put("frm", "application/x-frm");
			put("gbr", "application/x-gbr");
			put("gif", "image/gif");
			put("gp4", "application/x-gp4");
			put("hmr", "application/x-hmr");
			put("hpl", "application/x-hpl");
			put("hrf", "application/x-hrf");
			put("htc", "text/x-component");
			put("html", "text/html");
			put("htx", "text/html");
			put("ico", "image/x-icon");
			put("iff", "application/x-iff");
			put("igs", "application/x-igs");
			put("img", "application/x-img");
			put("isp", "application/x-internet-signup");
			put("java", "java/*");
			put("jpe", "image/jpeg");
			put("jpeg", "image/jpeg");
			put("jsp", "text/html");
			put("lar", "application/x-laplayer-reg");
			put("lavs", "audio/x-liquid-secure");
			put("lmsff", "audio/x-la-lms");
			put("ltr", "application/x-ltr");
			put("m2v", "video/x-mpeg");
			put("m4e", "video/mpeg4");
			put("man", "application/x-troff-man");
			put("mfp", "application/x-shockwave-flash");
			put("mhtml", "message/rfc822");
			put("mid", "audio/mid");
			put("mil", "application/x-mil");
			put("mnd", "audio/x-musicnet-download");
			put("mocha", "application/x-javascript");
			put("mp1", "audio/mp1");
			put("mp2v", "video/mpeg");
			put("mp4", "video/mp4");
			put("mpd", "application/vnd.ms-project");
			put("mpeg", "video/mpg");
			put("mpga", "audio/rn-mpeg");
			put("mps", "video/x-mpeg");
			put("mpv", "video/mpg");
			put("mpw", "application/vnd.ms-project");
			put("mtx", "text/xml");
			put("net", "image/pnetvue");
			put("nws", "message/rfc822");
			put("out", "application/x-out");
			put("p12", "application/x-pkcs12");
			put("p7c", "application/pkcs7-mime");
			put("p7r", "application/x-pkcs7-certreqresp");
			put("pc5", "application/x-pc5");
			put("pcl", "application/x-pcl");
			put("pdx", "application/vnd.adobe.pdx");
			put("pgl", "application/x-pgl");
			put("pko", "application/vnd.ms-pki.pko");
			put("plg", "text/html");
			put("plt", "application/x-plt");
			put("ppa", "application/vnd.ms-powerpoint");
			put("pps", "application/vnd.ms-powerpoint");
			put("prf", "application/pics-rules");
			put("prt", "application/x-prt");
			put("pwz", "application/vnd.ms-powerpoint");
			put("ra", "audio/vnd.rn-realaudio");
			put("ras", "application/x-ras");
			put("rdf", "text/xml");
			put("red", "application/x-red");
			put("rjs", "application/vnd.rn-realsystem-rjs");
			put("rlc", "application/x-rlc");
			put("rm", "application/vnd.rn-realmedia");
			put("rmi", "audio/mid");
			put("rmm", "audio/x-pn-realaudio");
			put("rms", "application/vnd.rn-realmedia-secure");
			put("rmx", "application/vnd.rn-realsystem-rmx");
			put("rp", "image/vnd.rn-realpix");
			put("rsml", "application/vnd.rn-rsml");
			put("rv", "video/vnd.rn-realvideo");
			put("sat", "application/x-sat");
			put("sdw", "application/x-sdw");
			put("slb", "application/x-slb");
			put("slk", "drawing/x-slk");
			put("smil", "application/smil");
			put("snd", "audio/basic");
			put("sor", "text/plain");
			put("spl", "application/futuresplash");
			put("ssm", "application/streamingmedia");
			put("stl", "application/vnd.ms-pki.stl");
			put("sty", "application/x-sty");
			put("swf", "application/x-shockwave-flash");
			put("tg4", "application/x-tg4");
			put("tif", "image/tiff");
			put("tiff", "image/tiff");
			put("top", "drawing/x-top");
			put("tsd", "text/xml");
			put("uin", "application/x-icq");
			put("vcf", "text/x-vcard");
			put("vdx", "application/vnd.visio");
			put("vpg", "application/x-vpeg005");
			put("vst", "application/vnd.visio");
			put("vsw", "application/vnd.visio");
			put("vtx", "application/vnd.visio");
			put("wav", "audio/wav");
			put("wb1", "application/x-wb1");
			put("wb3", "application/x-wb3");
			put("wiz", "application/msword");
			put("wk4", "application/x-wk4");
			put("wks", "application/x-wks");
			put("wma", "audio/x-ms-wma");
			put("wmf", "application/x-wmf");
			put("wmv", "video/x-ms-wmv");
			put("wmz", "application/x-ms-wmz");
			put("wpd", "application/x-wpd");
			put("wpl", "application/vnd.ms-wpl");
			put("wr1", "application/x-wr1");
			put("wrk", "application/x-wrk");
			put("ws2", "application/x-ws");
			put("wsdl", "text/xml");
			put("xdp", "application/vnd.adobe.xdp");
			put("xfd", "application/vnd.adobe.xfd");
			put("xhtml", "text/html");
			put("xml", "text/xml");
			put("xq", "text/xml");
			put("xquery", "text/xml");
			put("xsl", "text/xml");
			put("xwd", "application/x-xwd");
			put("sis", "application/vnd.symbian.install");
			put("x_t", "application/x-x_t");
			put("apk", "application/vnd.android.package-archive");
			put("301", "application/x-301");
			put("906", "application/x-906");
			put("a11", "application/x-a11");
			put("ai", "application/postscript");
			put("aifc", "audio/aiff");
			put("anv", "application/x-anv");
			put("asf", "video/x-ms-asf");
			put("asx", "video/x-ms-asf");
			put("avi", "video/avi");
			put("biz", "text/xml");
			put("bot", "application/x-bot");
			put("c90", "application/x-c90");
			put("cat", "application/vnd.ms-pki.seccat");
			put("cdr", "application/x-cdr");
			put("cer", "application/x-x509-ca-cert");
			put("cgm", "application/x-cgm");
			put("class", "java/*");
			put("cmp", "application/x-cmp");
			put("cot", "application/x-cot");
			put("crt", "application/x-x509-ca-cert");
			put("css", "text/css");
			put("dbf", "application/x-dbf");
			put("dbx", "application/x-dbx");
			put("dcx", "application/x-dcx");
			put("dgn", "application/x-dgn");
			put("dll", "application/x-msdownload");
			put("dot", "application/msword");
			put("dtd", "text/xml");
			put("dwf", "application/x-dwf");
			put("dxb", "application/x-dxb");
			put("edn", "application/vnd.adobe.edn");
			put("eml", "message/rfc822");
			put("epi", "application/x-epi");
			put("exe", "application/x-msdownload");
			put("fdf", "application/vnd.fdf");
			put("fo", "text/xml");
			put("g4", "application/x-g4");
			put("gl2", "application/x-gl2");
			put("hgl", "application/x-hgl");
			put("hpg", "application/x-hpgl");
			put("hqx", "application/mac-binhex40");
			put("hta", "application/hta");
			put("htm", "text/html");
			put("htt", "text/webviewhtml");
			put("icb", "application/x-icb");
			put("ig4", "application/x-g4");
			put("iii", "application/x-iphone");
			put("ins", "application/x-internet-signup");
			put("IVF", "video/x-ivf");
			put("jfif", "image/jpeg");
			put("jpg", "image/jpeg");
			put("js", "application/x-javascript");
			put("la1", "audio/x-liquid-file");
			put("latex", "application/x-latex");
			put("lbm", "application/x-lbm");
			put("ls", "application/x-javascript");
			put("m1v", "video/x-mpeg");
			put("m3u", "audio/mpegurl");
			put("mac", "application/x-mac");
			put("math", "text/xml");
			put("mdb", "application/x-mdb");
			put("mht", "message/rfc822");
			put("mi", "application/x-mi");
			put("midi", "audio/mid");
			put("mml", "text/xml");
			put("mns", "audio/x-musicnet-stream");
			put("movie", "video/x-sgi-movie");
			put("mp2", "audio/mp2");
			put("mp3", "audio/mp3");
			put("mpa", "video/x-mpg");
			put("mpe", "video/x-mpeg");
			put("mpg", "video/mpg");
			put("mpp", "application/vnd.ms-project");
			put("mpt", "application/vnd.ms-project");
			put("mpv2", "video/mpeg");
			put("mpx", "application/vnd.ms-project");
			put("mxp", "application/x-mmxp");
			put("nrf", "application/x-nrf");
			put("odc", "text/x-ms-odc");
			put("p10", "application/pkcs10");
			put("p7b", "application/x-pkcs7-certificates");
			put("p7m", "application/pkcs7-mime");
			put("p7s", "application/pkcs7-signature");
			put("pci", "application/x-pci");
			put("pcx", "application/x-pcx");
			put("pdf", "application/pdf");
			put("pfx", "application/x-pkcs12");
			put("pic", "application/x-pic");
			put("pl", "application/x-perl");
			put("pls", "audio/scpls");
			put("png", "image/png");
			put("pot", "application/vnd.ms-powerpoint");
			put("ppm", "application/x-ppm");
			put("ppt", "application/vnd.ms-powerpoint");
			put("pr", "application/x-pr");
			put("prn", "application/x-prn");
			put("ps", "application/x-ps");
			put("ptn", "application/x-ptn");
			put("r3t", "text/vnd.rn-realtext3d");
			put("ram", "audio/x-pn-realaudio");
			put("rat", "application/rat-file");
			put("rec", "application/vnd.rn-recording");
			put("rgb", "application/x-rgb");
			put("rjt", "application/vnd.rn-realsystem-rjt");
			put("rle", "application/x-rle");
			put("rmf", "application/vnd.adobe.rmf");
			put("rmj", "application/vnd.rn-realsystem-rmj");
			put("rmp", "application/vnd.rn-rn_music_package");
			put("rmvb", "application/vnd.rn-realmedia-vbr");
			put("rnx", "application/vnd.rn-realplayer");
			put("rpm", "audio/x-pn-realaudio-plugin");
			put("rt", "text/vnd.rn-realtext");
			put("rtf", "application/x-rtf");
			put("sam", "application/x-sam");
			put("sdp", "application/sdp");
			put("sit", "application/x-stuffit");
			put("sld", "application/x-sld");
			put("smi", "application/smil");
			put("smk", "application/x-smk");
			put("sol", "text/plain");
			put("spc", "application/x-pkcs7-certificates");
			put("spp", "text/xml");
			put("sst", "application/vnd.ms-pki.certstore");
			put("stm", "text/html");
			put("svg", "text/xml");
			put("tdf", "application/x-tdf");
			put("tga", "application/x-tga");
			put("tld", "text/xml");
			put("torrent", "application/x-bittorrent");
			put("txt", "text/plain");
			put("uls", "text/iuls");
			put("vda", "application/x-vda");
			put("vml", "text/xml");
			put("vsd", "application/vnd.visio");
			put("vss", "application/vnd.visio");
			put("vsx", "application/vnd.visio");
			put("vxml", "text/xml");
			put("wax", "audio/x-ms-wax");
			put("wb2", "application/x-wb2");
			put("wbmp", "image/vnd.wap.wbmp");
			put("wk3", "application/x-wk3");
			put("wkq", "application/x-wkq");
			put("wm", "video/x-ms-wm");
			put("wmd", "application/x-ms-wmd");
			put("wml", "text/vnd.wap.wml");
			put("wmx", "video/x-ms-wmx");
			put("wp6", "application/x-wp6");
			put("wpg", "application/x-wpg");
			put("wq1", "application/x-wq1");
			put("wri", "application/x-wri");
			put("ws", "application/x-ws");
			put("wsc", "text/scriptlet");
			put("wvx", "video/x-ms-wvx");
			put("xdr", "text/xml");
			put("xfdf", "application/vnd.adobe.xfdf");
			put("xls", "application/vnd.ms-excel");
			put("xlw", "application/x-xlw");
			put("xpl", "audio/scpls");
			put("xql", "text/xml");
			put("xsd", "text/xml");
			put("xslt", "text/xml");
			put("x_b", "application/x-x_b");
			put("sisx", "application/vnd.symbian.install");
			put("ipa", "application/vnd.iphone");
			put("xap", "application/x-silverlight-app");
		}
	};

	/**
	 * 根据文件的后缀，返回对应html的Content-Type
	 * @param fileExt
	 * @return
	 */
	public static String getContentType(String fileExt) {
		final String defaultContentType = "application/octet-stream";
		if(fileExt == null)
			return defaultContentType;
		String fileExtLow = fileExt.toLowerCase();
		return CONTENT_MAP.getOrDefault(fileExtLow, defaultContentType);
	}

}
