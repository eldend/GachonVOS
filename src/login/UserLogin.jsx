import { Link } from 'react-router-dom';
import styles from './login.module.css';
import Header from '../main/Header';
import Footer from '../main/Footer';

export default function UserLogin() {
    return (
        <div><Header/>
        <div className={styles.page}>
            <form className="form-signin" method="post" action="/login-proc">
                <div className={styles.titleWrap}>
                    로그인
                </div>
                <div className={styles.subtitleWrap}>
                    가천대 VOS에 오신 것을 환영합니다.
                </div>

                <div className={styles.contentWrap}>
                    <div className={styles.inputWrap}>
                        <input className={styles.input} 
                                name="userid"
                                placeholder='아이디를 입력하세요.'/>
                    </div>
                    <div className={styles.inputWrap}>
                        <input className={styles.input}
                            type='password'
                            placeholder='비밀번호를 입력하세요.'
                            name="pw"
/>
                    </div>
                </div>

                <div>
                    <button 
                    className={styles.bottomBtn}
                    type='submit'>
                        로그인
                    </button>
                </div>
            </form>
        </div><Footer/></div>
    );
}