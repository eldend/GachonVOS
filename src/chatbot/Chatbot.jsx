import React, { useState, useRef, useEffect } from 'react';
import './chatbot.css';
import axios from 'axios';
import bot from '../img/bot.png';

const Chatbot = () => {
    const [messages, setMessages] = useState([]);
    const inputRef = useRef();
    const chatWrapRef = useRef();

    useEffect(() => {
        scrollToBottom();
    }, [messages]);

    const scrollToBottom = () => {
        chatWrapRef.current.scrollTop = chatWrapRef.current.scrollHeight;
    };

    const [briefingFetched, setBriefingFetched] = useState(false);

    useEffect(() => {
        const fetchBriefingMessage = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/chat-gpt/briefing');
                const briefingMessage = response.data.text;
                setMessages([{ type: 'chatbot', message: briefingMessage }]);
                setBriefingFetched(true);
            } catch (error) {
                console.error('브리핑 메시지를 가져오는 중 오류 발생:', error);
            }
        };

        if (!briefingFetched) {
            fetchBriefingMessage();
        }
    }, [briefingFetched]);

    const handleUserMessage = async (e) => {
        e.preventDefault();
        const userMessage = inputRef.current.value;
        setMessages((prevMessages) => [...prevMessages, { type: 'user', message: userMessage }]);
        inputRef.current.value = '';

        try {
            const response = await axios.post('http://localhost:8080/api/chat-gpt/question', {
                question: userMessage,
            });

            const chatbotMessage = response.data.text;
            setMessages((prevMessages) => [...prevMessages, { type: 'chatbot', message: chatbotMessage }]);
        } catch (error) {
            console.error('메시지를 전송하는 중 오류 발생:', error);
        }
    };

    return (
        <div>
            <div className='chatBotPage'>
                <div className='chattingWrap' ref={chatWrapRef}>
                    <div className='briefWrap'>                   
                        <img className='bot' src={bot} height='45px' width='32px'/>
                        <div className='brief'>
                            관리자님 반갑습니다! 브리핑을 준비중입니다. 잠시만 기다려주세요.
                        </div>
                    </div>
                    {messages.map((msg, index) => (
                        <div className={`chatting ${msg.type === 'user' ? 'rightAlign' : 'leftAlign'}`} key={index}>
                            <div className="messageType">
                                {msg.type === 'user' ? '' : <img className='bot' src={bot} height='45px' width='32px'/>}
                            </div>
                            <div className="messageContent">
                                {msg.message}
                            </div>
                        </div>
                    
                    ))}
                </div>
                <div className='writeMessageWrap'>
                    <form onSubmit={handleUserMessage}>
                        <input className='writeInput' ref={inputRef} type='text' placeholder='메시지를 입력하세요.'/>
                        <div className='sendWrap'>
                            <button className='send' type='submit'>Send</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default Chatbot;