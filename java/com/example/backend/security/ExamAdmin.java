//package com.example.backend.security;
//
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//
//import java.util.Collection;
//
//@Getter
//@Setter
//public class ExamAdmin extends User {
//    String id;
//    String name;
//
//    public ExamAdmin(Admin admin, Collection<? extends GrantedAuthority> authorities) {
//        super(admin.getId(), admin.getPassword(), authorities);
//        this.id = admin.getId();
//        this.name = admin.getName();
//    }
//}
