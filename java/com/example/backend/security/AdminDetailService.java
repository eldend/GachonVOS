//package com.example.backend.security;
//
//import com.example.backend.application.dao.AdminRepository;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@RequiredArgsConstructor
//public class AdminDetailService implements UserDetailsService {
//
//    private final Logger logger = LoggerFactory.getLogger(AdminDetailService.class);
//
//    private final AdminRepository adminRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String adminId) throws UsernameNotFoundException {
//        logger.info("adminId === "+adminId);
//        KafkaProperties.Admin admin = adminRepository.findOneById(adminId);
//        if(admin == null){
//            throw new UsernameNotFoundException("해당 관리자가 없습니다.");
//        }
//        ExamAdmin examAdmin = new ExamAdmin(admin, buildAdminAuthority());
//
//        return examAdmin;
//    }
//
//    private Set<GrantedAuthority> buildAdminAuthority() {
//
//        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
//
//        setAuths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//
//        return setAuths;
//    }
//}
