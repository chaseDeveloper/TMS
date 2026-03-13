package tms.adminPool.service;


import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {	
		
	@Autowired
	private JavaMailSenderImpl mailSender;
	
	private Log debugLog = LogFactory.getLog(this.getClass().getName());
	
	public void sendPwdMail(Map<String, Object> mailInfoMap)  throws Exception  {    	
  
		String mailAuth = (String) mailSender.getJavaMailProperties().get("kediMailAuth");
		String mailTls = (String) mailSender.getJavaMailProperties().get("kediMailTls");
		String mailPort = (String) mailSender.getJavaMailProperties().get("kediMailPort");
		String mailHost = (String) mailSender.getJavaMailProperties().get("kediMailHost");		
		String mailId =(String) mailSender.getJavaMailProperties().get("kediMailId");
		String mailPasswd = (String) mailSender.getJavaMailProperties().get("kediMailPasswd");

		String mailVendor = (String) mailSender.getJavaMailProperties().get("kediMailVendor");
		String mailFromEmail = (String) mailSender.getJavaMailProperties().get("kediMailFromEmail");
		
		Authenticator auth = new MyAuthenticator(mailId, mailPasswd);
		
		Properties props = new Properties();
		
	
		props.put("mail.smtp.auth", mailAuth);
		props.put("mail.smtp.starttls.enable", mailTls);
		props.put("mail.smtp.port", mailPort);
		props.put("mail.host", mailHost);
		//props.put("mail.smtp.debug", "true");
		
		if(mailVendor.equals("naver")) {
			props.put("mail.smtp.ssl.enable", "true");	
			props.put("mail.smtp.ssl.trust", "*");	
			props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		}
		
		Session session = Session.getInstance(props, auth);
	    session.setDebug(true);
	    //String fromEmail = "teva@kedi.re.kr";
	    //String fromEmail = "tyuhm@kedi.re.kr";
	    String fromEmail = mailFromEmail;
	    String fromName = "한국교육개발원";	    
	    
	    String mailSubject= "[한국교육개발원] 교원양성과정 역량진단 진단위원 공모시스템 개인정보동의 안내";
	    
    	try {
        	MimeMessage msg = new MimeMessage(session);

        	//String tmpPwd = (String)mailInfoMap.get("tmpPws");
        	//String univNm = (String)mailInfoMap.get("univNm");
        	String univNm = (String)mailInfoMap.get("univNm");
        	String univCd = (String)mailInfoMap.get("univCd");
        	String usrNm = (String)mailInfoMap.get("usrNm");
        	String usrId = (String)mailInfoMap.get("usrId");
        	String usrPerm = (String)mailInfoMap.get("usrPerm");
	    	String toEmail = (String) mailInfoMap.get("usrEmail");
	    	String PRIV_DTTM = (String) mailInfoMap.get("PRIV_DTTM");
	    	
	    	String after2Years = LocalDate.parse(PRIV_DTTM, DateTimeFormatter.ofPattern("yyyy/MM/dd"))
	    		    .plusYears(2)
	    		    .format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
	    	
	    	toEmail = toEmail.trim(); 
	    	toEmail = toEmail.replaceAll(" ", "");
	    	
/*	    	if(usrPerm.equals("A")) {
    			//univNm = "KEDI";
    			univCd = usrId;
    		} else if(usrPerm.equals("E")) {
    			//univNm = "교육부";
    			univCd = usrId;
    		}*/
	    	
	    	univNm = univCd;
	    	
    		StringBuilder sb = new StringBuilder();
    		
			sb.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width: 800px;\">")
			  .append("<tbody><tr><td style=\"width: 50px;\"></td>")
			  .append("<td style=\"width: 615px; font-size: 13px; color: #333;\">")
			    .append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width: 100%; border:1px solid #c7c7c7; margin-top:20px;\">")
			      .append("<tbody><tr>")
			        .append("<td style=\"background:#f2f2f2; font-size: 25px; font-weight: bolder; letter-spacing: -1px; padding:60px 60px 0 60px\">")
			        .append("회원정보 재동의 시행을 안내드립니다.</td>")
			      .append("</tr><tr>")
			        .append("<td style=\"background:#f2f2f2; width: 453px; font-size: 14px; color: #747474; ")
			        .append("line-height: 22px; vertical-align: bottom; padding:20px 60px 10px 60px\">")
			        .append("2022년 11월 17일부터 개인정보 보호법에 따른 2년주기 회원정보 재동의 절차를 시행합니다.<br>")
			        .append("“"+usrNm+"“ 회원님께서는 "+after2Years+"까지 한국교육개발원 교원양성기관 역량진단 진단위원 공모시스템에 재동의 하지 않을 경우, ")
			        .append("자동으로 회원탈퇴 처리됨을 알려드립니다.</td>")
			      .append("</tr><tr>")
			        .append("<td style=\"height: 30px; background:#f2f2f2;\" colspan=\"2\"></td>")
			      .append("</tr><tr>")
			        .append("<td colspan=\"2\" style=\"background:#f2f2f2;\">")
			          .append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" ")
			          .append("style=\"width: 85%; background:#fff; margin:auto; border:1px solid #ddd \">")
			            .append("<tbody><tr><td style=\"padding:30px\">")
			            .append("[회원정책 변경 안내]<br>")
			            .append("- 2019년 1월 1일부터 보다 편리하고 안전한 한국교육개발원 교원양성기관 역량진단 진단위원 공모시스템의 이용을 위해 ")
			            .append("개인정보보호법에 따른 회원정보 재동의를 시행하고 있습니다.표준개인정보보호지침 (안정행정부 2011.09)에 의거 ")
			            .append("2년을 주기로 재동의 절차를 거쳐 동의한 경우에만 회원자격을 유지할 수 있습니다.")
			            .append("</td></tr></tbody>")
			          .append("</table>")
			        .append("</td>")
			      .append("</tr><tr>")
			        .append("<td colspan=\"2\" style=\"font-size: 13px; color: #747474; line-height: 22px; letter-spacing: -0.04em; ")
			        .append("background:#f2f2f2; padding:20px 60px 40px 60px; border-bottom:1px solid #ddd\">")
			          .append("<strong style=\"color:#333\">주요내용</strong>")
			          .append("<p>- "+after2Years+" 까지 정보제공 재동의를 하지 않는 경우 회원 개인정보 삭제 및 자동 탈퇴처리</p>")
			          .append("<strong style=\"color:#333\">개인정보 파기 및 탈퇴 처리 실행일자</strong>")
			          .append("<p>- "+after2Years+"(+1Day)</p>")
			          .append("<strong style=\"color:#333\">개인정보 파기 대상 개인정보 항목</strong>")
			          .append("<p>- 회원가입 시 입력한 모든 정보는 삭제 처리되고 아이디는 재사용 및 복구 불가능합니다.</p>")
			          .append("<strong style=\"color:#333\">관련법령</strong>")
			          .append("<p>- 표준개인정보보호지침 제64조(개인정보파일 보유기간의 산정)<br>")
			          .append("제③항 정채고객, 홈페이지회원 동의 홍보 및 대국민 서비스 목적의 외부고객 명부는 특별한 경우를 제외하고는 ")
			          .append("2년을 주기로 정보주체의 재동의 절차를 거쳐 동의한 경우에만 계속적으로 보유할 수 있다.</p>")
			          .append("<p style=\"padding-left:130px; margin-top:40px\">")
			          .append("<a href=\"https://necte.kedi.re.kr/apply/\" style=\"text-decoration: none;\" target=\"_blank\" ")
			          .append("title=\"한국교육개발원 교원양성과정 역량진단 진단위원 공모시스템 새창\" rel=\"noreferrer noopener\">")
			          .append("<span style=\"height:100px; padding:15px 10px; background:#f79646; color:#fff; border:1px solid #d9d9d9; margin-top:30px\">")
			          .append("교원양성과정 역량진단 진단위원 공모시스템 바로가기</span></a></p>")
			        .append("</td>")
			      .append("</tr><tr>")
			        .append("<th colspan=\"3\" rowspan=\"1\" style=\"height: 96px; text-align:center; padding:10px 0;\">")
			          .append("<a href=\"https://necte.kedi.re.kr/apply/\" target=\"_blank\" ")
			          .append("title=\"교원양성과정 역량진단 진단위원 공모시스템 새창\" rel=\"noreferrer noopener\">")
			          .append("<img src=\"https://necte.kedi.re.kr/necte/images/kedi_logo.png\" ")
			          .append("style=\"width:214px; height:50px;\" alt=\"교원양성과정 역량진단 진단위원 공모시스템 로고\" loading=\"lazy\">")
			          .append("</a>")
			        .append("</th>")
			      .append("</tr><tr>")
			        .append("<td style=\"background:#17375e; padding:10px 60px; color:#fff\">")
			          .append("<p>본 메일은 발신 전용 메일입니다.<br>")
			          .append("Copyright ⓒ 2017 KEDI. All Rights Reserved.</p>")
			        .append("</td>")
			      .append("</tr></tbody>")
			    .append("</table></td>")
			    .append("<td style=\"width: 50px;\"></td>")
			  .append("</tr></tbody></table>");

			//https://necte.kedi.re.kr/apply
			msg.setContent(sb.toString(), "text/html; charset=euc-kr");
	    	
	    	msg.setFrom(new InternetAddress(fromEmail, fromName));
	    	String toName = "";
    		if(usrPerm.equals("A")) {
    			toName = "관리자" + "(" + usrNm + ")";
    		} else if(usrPerm.equals("A")) {
    			toName = "교육부" + "(" + usrNm + ")";
    		} else {
    			toName = usrNm;
    		}
    		
    		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail, toName));	    	
	    	msg.setSubject(mailSubject);
	    	
	    	
	    	//임시 주석 메일 발송전 데이터 생성 테스트
	    	Transport.send(msg);    	
	    	
	    	debugLog.debug(toName + " " + toEmail +  "로  발송 성공");
	    	 
	    	
    	} catch(Exception e) { 
    		String univNm =  (String)mailInfoMap.get("univNm"); 
    		String usrEmail = (String) mailInfoMap.get("usrEmail");
    		String usrPerm = (String)mailInfoMap.get("usrPerm");
    		String usrNm = (String)mailInfoMap.get("usrNm");
    		String rmrk = null;
    		
    		String toName = "";
    		if(usrPerm.equals("A")) {
    			toName = "관리자" + "(" + usrNm + ")";
    		} else if(usrPerm.equals("A")) {
    			toName = "교육부" + "(" + usrNm + ")";
    		} else {
    			toName = usrNm;
    		}
    		
    		rmrk =   toName + " "  + usrEmail +  "발송 실패";
    		debugLog.debug(rmrk);    	
    	} 
    }
	
	public void sendJoinPwdMail(Map<String, Object> mailInfoMap)  throws Exception  {    	
		  
		String mailAuth = (String) mailSender.getJavaMailProperties().get("kediMailAuth");
		String mailTls = (String) mailSender.getJavaMailProperties().get("kediMailTls");
		String mailPort = (String) mailSender.getJavaMailProperties().get("kediMailPort");
		String mailHost = (String) mailSender.getJavaMailProperties().get("kediMailHost");		
		String mailId =(String) mailSender.getJavaMailProperties().get("kediMailId");
		String mailPasswd = (String) mailSender.getJavaMailProperties().get("kediMailPasswd");

		String mailVendor = (String) mailSender.getJavaMailProperties().get("kediMailVendor");
		String mailFromEmail = (String) mailSender.getJavaMailProperties().get("kediMailFromEmail");
		
		Authenticator auth = new MyAuthenticator(mailId, mailPasswd);
		
		Properties props = new Properties();
		
	
		props.put("mail.smtp.auth", mailAuth);
		props.put("mail.smtp.starttls.enable", mailTls);
		props.put("mail.smtp.port", mailPort);
		props.put("mail.host", mailHost);
		//props.put("mail.smtp.debug", "true");
		
		if(mailVendor.equals("naver")) {
			props.put("mail.smtp.ssl.enable", "true");	
			props.put("mail.smtp.ssl.trust", "*");	
			props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		}
		
		Session session = Session.getInstance(props, auth);
	    session.setDebug(true);
	    //String fromEmail = "teva@kedi.re.kr";
	    //String fromEmail = "tyuhm@kedi.re.kr";
	    String fromEmail = mailFromEmail;
	    String fromName = "한국교육개발원";	    
	    
	    String mailSubject= "[한국교육개발원] 교원양성기관 역량진단 진단위원 후보자 공모시스템 신규 구축에 따른 서비스 전환 안내";
	    
    	try {
        	MimeMessage msg = new MimeMessage(session);

        	String univNm = (String)mailInfoMap.get("univNm");
        	String univCd = (String)mailInfoMap.get("univCd");
        	String usrNm = (String)mailInfoMap.get("usrNm");
        	String usrId = (String)mailInfoMap.get("usrId");
        	String usrPerm = (String)mailInfoMap.get("usrPerm");
	    	String toEmail = (String) mailInfoMap.get("usrEmail");
	    	String PRIV_DTTM = (String) mailInfoMap.get("PRIV_DTTM");
	    	toEmail = toEmail.trim(); 
	    	toEmail = toEmail.replaceAll(" ", "");
	    	
/*	    	if(usrPerm.equals("A")) {
    			//univNm = "KEDI";
    			univCd = usrId;
    		} else if(usrPerm.equals("E")) {
    			//univNm = "교육부";
    			univCd = usrId;
    		}*/
	    	
    		StringBuilder sb = new StringBuilder();
    		
    		sb.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width: 800px;\">")
    		  .append("<tbody><tr>")
    		    .append("<td style=\"width: 50px;\"></td>")
    		    .append("<td style=\"width: 615px; font-size: 13px; color: #333;\">")
    		      .append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width: 100%; border:1px solid #c7c7c7; margin-top:20px;\">")
    		        .append("<tbody>")

    		          // 제목
    		          .append("<tr>")
    		            .append("<td style=\"background:#f2f2f2; font-size:25px; font-weight:bold; letter-spacing:-1px; padding:60px 60px 0 60px\">")
    		              .append("회원가입 방법을 안내 드립니다.")
    		            .append("</td>")
    		          .append("</tr>")

    		          // 본문
    		          .append("<tr>")
    		            .append("<td style=\"background:#f2f2f2; width:453px; font-size:14px; color:#747474; line-height:22px; vertical-align:bottom; padding:20px 60px 10px 60px\">")
    		              .append("안녕하세요. “"+usrNm+"” 회원님께 6주기 교원양성기관 역량진단 진단위원 공모시스템 회원가입 방법을 안내 드립니다.<br><br>")
    		              .append("아래의 기간에 회원가입을 진행하시면, 5주기 교원양성기관 역량진단 진단위원 후보자 정보가 <strong>6주기 교원양성기관 역량진단 진단위원 후보자 공모시스템</strong>으로 자동 승계되오니, 많은 참여 부탁드립니다.<br><br>")
    		              
    		              .append("□ 주요 내용<br>")
    		              .append(" · 6주기 교원양성기관 역량진단 진단위원 공모 시스템 회원 가입<br><br>")
    		              
    		              .append("□ 회원 가입 기간: <strong>2025.2.3.(월) ~ 2.28.(금)</strong><br>")
    		              .append(" ※ 해당 기간 이후 데이터 이관 불가<br><br>")
    		              
    		              .append("□ 회원 가입 진행<br>")
    		              .append(" · 교원양성기관 역량진단 진단위원 공모시스템(<a href=\"https://necte.kedi.re.kr/apply\" target=\"_blank\">https://necte.kedi.re.kr/apply</a>) 접속<br>")
    		              .append(" · 상단의 [회원 가입] 버튼 클릭 후 가입 절차 진행<br><br>")
    		              
    		              .append("□ 주의 사항<br>")
    		              .append(" · 회원 가입 기간 이후 5주기 진단위원 후보자 등록 정보는 자동 삭제되며, 복구할 수 없습니다.<br>")
    		              .append(" · 기간 이후에 회원 가입을 진행하는 경우, 모든 정보를 새로 입력해야 하므로 양지하여 주시기 바랍니다.<br><br>")
    		              
    		              .append("한국교육개발원은 개인정보 보호와 서비스 품질 개선을 위해 항상 노력하고 있습니다.<br><br>")
    		              .append("감사합니다.")
    		            .append("</td>")
    		          .append("</tr>")

    		          // 여백
    		          .append("<tr>")
    		            .append("<td style=\"height: 30px; background:#f2f2f2;\" colspan=\"2\"></td>")
    		          .append("</tr>")

    		          // 버튼(시스템 바로가기)
    		          .append("<tr>")
    		            .append("<td colspan=\"2\" style=\"background:#f2f2f2; text-align:center; padding-bottom:20px;\">")
    		              .append("<a href=\"https://necte.kedi.re.kr/apply/\" style=\"text-decoration:none;\" target=\"_blank\" title=\"교원양성과정 역량진단 진단위원 공모시스템 새창\" rel=\"noreferrer noopener\">")
    		                .append("<span style=\"display:inline-block; padding:15px 30px; background:#f79646; color:#fff; border:1px solid #d9d9d9; margin-top:30px\">")
    		                  .append("교원양성과정 역량진단 진단위원 공모시스템 바로가기")
    		                .append("</span>")
    		              .append("</a>")
    		            .append("</td>")
    		          .append("</tr>")

    		          // 로고
    		          .append("<tr>")
    		            .append("<th colspan=\"3\" style=\"height:96px; text-align:center; padding:10px 0;\">")
    		              .append("<a href=\"https://necte.kedi.re.kr/apply/\" target=\"_blank\" title=\"교원양성과정 역량진단 진단위원 공모시스템 새창\" rel=\"noreferrer noopener\">")
    		                .append("<img src=\"https://necte.kedi.re.kr/necte/images/kedi_logo.png\" style=\"width:214px; height:50px;\" alt=\"교원양성과정 역량진단 진단위원 공모시스템 로고\" loading=\"lazy\">")
    		              .append("</a>")
    		            .append("</th>")
    		          .append("</tr>")

    		          // 하단 영역
    		          .append("<tr>")
    		            .append("<td style=\"background:#17375e; padding:10px 60px; color:#fff\">")
    		              .append("<p style=\"margin:0\">본 메일은 발신 전용 메일입니다.<br>")
    		              .append("Copyright ⓒ 2017 KEDI. All Rights Reserved.</p>")
    		            .append("</td>")
    		          .append("</tr>")

    		        .append("</tbody>")
    		      .append("</table>")
    		    .append("</td>")
    		    .append("<td style=\"width: 50px;\"></td>")
    		  .append("</tr></tbody>")
    		.append("</table>");

			
			msg.setContent(sb.toString(), "text/html; charset=euc-kr");
	    	
	    	msg.setFrom(new InternetAddress(fromEmail, fromName));
	    	String toName = "";
    		if(usrPerm.equals("A")) {
    			toName = "관리자" + "(" + usrNm + ")";
    		} else if(usrPerm.equals("A")) {
    			toName = "교육부" + "(" + usrNm + ")";
    		} else {
    			toName = usrNm;
    		}
    		
    		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail, toName));	    	
	    	msg.setSubject(mailSubject);
	    	
	    	
	    	//임시 주석 메일 발송전 데이터 생성 테스트
	    	Transport.send(msg);    	
	    	
	    	debugLog.debug(toName + " " + toEmail +  "로  발송 성공");
	    	 
	    	
    	} catch(Exception e) { 
    		String univNm =  (String)mailInfoMap.get("univNm"); 
    		String usrEmail = (String) mailInfoMap.get("usrEmail");
    		String usrPerm = (String)mailInfoMap.get("usrPerm");
    		String usrNm = (String)mailInfoMap.get("usrNm");
    		String rmrk = null;
    		
    		String toName = "";
    		if(usrPerm.equals("A")) {
    			toName = "관리자" + "(" + usrNm + ")";
    		} else if(usrPerm.equals("A")) {
    			toName = "교육부" + "(" + usrNm + ")";
    		} else {
    			toName = usrNm;
    		}
    		
    		rmrk =   toName + " "  + usrEmail +  "발송 실패";
    		debugLog.debug(rmrk);    	
    	} 
    }

	public class MyAuthenticator extends javax.mail.Authenticator {

	    private String id;
	    private String pw;

	    public MyAuthenticator(String id, String pw) {
	        this.id = id;
	        this.pw = pw;
	    }

	    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
	        return new javax.mail.PasswordAuthentication(id, pw);
	    }
	}
}