import React from 'react';
import step from "../img/step.png";
import "./intro.css"
import Header from '../main/Header';
import Footer from '../main/Footer';

export default function UserLogin() {
    return (
        <div><Header/>
        <div className="introPage">
            <div className='intro1'>
                Gachon VOS 소개
            </div>
            <div className='introBox'>
                <div className='introContent'>
                    - 학생들에게 더 좋은 학교를 만들기 위한 Gachon VOS(Voice Of Student)에 오신 것을 환영합니다.
                </div>
                <div className='introContent'>
                    - Gachon VOS는 캠퍼스 생활 중 제안 사항이나 불편함을 건의하는 소통의 장입니다.
                </div>
            </div>
            <div className='intro2'>
                진행개요
            </div>
            <img id="stepimg" src={step} width="1000px" height="220px" />
        </div><Footer/></div>
    );
}