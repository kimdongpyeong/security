package kr.supporti.api.app.service;

import org.springframework.stereotype.Service;

@Service
public class ApiAppEmailService {

//    @Autowired
//    private UserAuthRepository userAuthRepository;
//
//    @Autowired
//    private MailUtil mailUtil;
//
//    @Value("${supporti.company.eamil-address}")
//    private String companyEmailAddress;
//
//    @Transactional
//    public Object emailAutn(HttpServletRequest request, EmailParamDto emailParamDto) {
//
//        String servletPath = this.getClass().getResource("/").getPath();
//        String html = this.readLineByLineJava8(
//                servletPath.replaceFirst("/", "") + "static/vue/page/template/email_auth_send.html");
//
//        // 임의의 문자열 생성
//        int leftLimit = 48;
//        int rightLimit = 122;
//        int targetStringLength = 8;
//        Random random = new Random();
//        String emailAutnCode = random.ints(leftLimit, rightLimit + 1)
//                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)).limit(targetStringLength)
//                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
//
//        html = html.replace("@emailAuthCode", emailAutnCode);
//
//        emailParamDto.setCode(emailAutnCode);
//        emailParamDto.setEmail(emailParamDto.getEmail());
//        userAuthMapper.authCodeComparison(emailParamDto);
//
//        List<String> toList = new ArrayList<>();
//        toList.add("<" + emailParamDto.getEmail() + ">");
//        List<String> ccList = new ArrayList<>();
//        MailDto mailDto = new MailDto();
//        mailDto.setFrom(companyEmailAddress);
//        mailDto.setToList(toList);
//        mailDto.setCcList(ccList);
//        mailDto.setSubject("Z-PLAY 인증 코드 발송");
//        mailDto.setText(html);
//
//        try {
//            mailUtil.sendMail(mailDto);
//            return "success";
//        } catch (MessagingException | IOException e) {
//            return "failed";
//        }
//    }
//
//    private String readLineByLineJava8(String filePath) {
//        StringBuilder contentBuilder = new StringBuilder();
//
//        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
//            stream.forEach(s -> contentBuilder.append(s).append("\n"));
//        } catch (IOException e) {
//        }
//
//        return contentBuilder.toString();
//    }
//
//    @Transactional
//    public Object emailAutnComparison(@Valid EmailParamDto emailParamDto) {
//        return userAuthMapper.emailSecurityCode(emailParamDto);
//    }
}