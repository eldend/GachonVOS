import React, { useState, useEffect } from 'react';
import styles from './signup.module.css';
import axios from 'axios';
import Header from '../main/Header';
import Footer from '../main/Footer';
export default function Signup() {
  const [email, setEmail] = useState('');
  const [pw, setPw] = useState('');
  const [pwConfirm, setPwConfirm] = useState('');
  const [emailValid, setEmailValid] = useState(false);
  const [pwMatch, setPwMatch] = useState(true);

  useEffect(() => {
    const form = document.getElementById('join_form');

    const handleSubmit = (e) => {
      e.preventDefault();

      const data = new FormData(form);
      const param = JSON.stringify(Object.fromEntries(data));

      axios.post('http://localhost:8080/api/user', param, {
        headers: {
          'Content-Type': 'application/json',
        },
      })
        .then(response => {
          if (response.status === 200) {
            window.location.href = 'http://localhost:3000/userlogin';
            alert('회원가입 성공');
          } else {
            alert('회원가입 실패');
          }
        })
        .catch(error => console.log(error));
    };

    if (form) {
      form.addEventListener('submit', handleSubmit);

      return () => {
        // 정리: 컴포넌트가 언마운트될 때 이벤트 리스너를 제거합니다.
        form.removeEventListener('submit', handleSubmit);
      };
    }
  }, []); // 빈 의존성 배열은 이 효과가 초기 렌더링 이후에 한 번 실행되도록 보장합니다.

  const handleEmail = (e) => {
    setEmail(e.target.value);
    const regex = /^(([^<>()\[\].,;:\s@"]+(\.[^<>()\[\].,;:\s@"]+)*)|(".+"))@gachon\.ac\.kr$/;
    setEmailValid(regex.test(e.target.value));
  };

  const handlePassword = (e) => {
    setPw(e.target.value);
    setPwMatch(e.target.value === '' ? true : pwConfirm === e.target.value);
  };

  const handlePasswordConfirm = (e) => {
    setPwConfirm(e.target.value);
    setPwMatch(e.target.value === '' ? true : pw === e.target.value);
  };

  return (
    <div>
    <Header/>
    <div className={styles.page}>
      <form className="form-signin" id='join_form'>
        <div className={styles.titleWrap}>
          회원가입
        </div>
        
        <div className={styles.contentWrap}>
          <div className={styles.inputTitle}>아이디</div>
          <div className={styles.inputWrap}>
            <input className={styles.input}
            id="username" name="username"/>
          </div>

          <div className={styles.inputTitle}>비밀번호</div>
          <div className={styles.inputWrap}>
            <input
              className={styles.input}
              type='password'
              value={pw}
              onChange={handlePassword}
              id="password" name="password"
            />
          </div>

          <div className={styles.inputTitle}>비밀번호 재확인</div>
          <div className={styles.inputWrap}>
            <input
              className={styles.input}
              type='password'
              value={pwConfirm}
              onChange={handlePasswordConfirm}
            />
          </div>
          <div className={styles.correctMessageWrap}>
            {pwMatch ? (pw !== '' && pwConfirm !== '' ? '* 비밀번호가 일치합니다.' : '') : ''}
          </div>
          <div className={styles.errorMessageWrap}>
            {!pwMatch && '* 비밀번호가 일치하지 않습니다.'}
          </div>
          
          <div className={styles.inputTitle}>학번</div>
          <div className={styles.inputWrap}>
            <input className={styles.input}
            id="hakbun" name="hakbun"/>
          </div>

          <div className={styles.inputTitle}>이메일</div>
          <div className={styles.inputWrap}>
              <input
                className={styles.input}
                placeholder='@gachon.ac.kr'
                value={email}
                onChange={handleEmail}
                id="email" name="email"
              />
          </div>
          <div className={styles.errorMessageWrap}>
            {!emailValid && email.length > 0 && (
              <div>* 올바른 이메일을 입력해주세요.</div>
            )}
          </div>
        </div>
        <div>
          <button className={styles.bottomBtn} type='submit'>
            가입하기
          </button>
        </div>
      </form>
    </div>
    <Footer/>
    </div>
  );
}
