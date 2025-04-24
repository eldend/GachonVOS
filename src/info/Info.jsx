import "./Info.css";
import Header from '../main/Header';
import Footer from '../main/Footer';

export default function Info() {
  return (
      <div><Header/>
    <div>
      <table>
        <thead>
          <tr>
            <th>학교기관</th>
            <th>담당부서</th>
            <th>연락처</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td rowSpan="2">교무처</td>
            <td>학사지원팀</td>
            <td>031-750-5045~9</td>
          </tr>
          <tr>
            <td>기획팀</td>
            <td>031-750-5016~8</td>
          </tr>
          <tr>
            <td rowSpan="2">학생복지처</td>
            <td>학생지원팀</td>
            <td>031-750-5051~5055</td>
          </tr>
          <tr>
            <td>장학복지팀</td>
            <td>031-750-5056~59/5130</td>
          </tr>
          <tr>
            <td>입학처</td>
            <td>입학팀</td>
            <td>031-750-5702~6/5902</td>
          </tr>
          <tr>
            <td rowSpan="6">총무팀</td>
            <td>총무인사팀</td>
            <td>031-750-5061~5065</td>
          </tr>
          <tr>
            <td>재무회계팀</td>
            <td>031-750-5081~3</td>
          </tr>
          <tr>
            <td>구매팀</td>
            <td>031-750-5301~2</td>
          </tr>
          <tr>
            <td>개인정보보호팀</td>
            <td>031-750-5544~5/5061</td>
          </tr>
          <tr>
            <td>시설운영팀</td>
            <td>031-750-****</td>
          </tr>
          <tr>
            <td>관재팀</td>
            <td>031-750-****</td>
          </tr>
          <tr>
            <td rowSpan="4">대외협력처</td>
            <td>홍보팀</td>
            <td>031-750-5675~9</td>
          </tr>
          <tr>
            <td>대외협력팀</td>
            <td>031-750-****</td>
          </tr>
          <tr>
            <td>발전기금팀</td>
            <td>031-750-5267/5268</td>
          </tr>
          <tr>
            <td>ESG센터</td>
            <td>031-750-5780~1</td>
          </tr>
          <tr>
            <td rowSpan="3">국제교류처</td>
            <td>외국인입학팀</td>
            <td>031-750-5838</td>
          </tr>
          <tr>
            <td>외국인 유학생 서비스팀</td>
            <td>031-750-5466</td>
          </tr>
          <tr>
            <td>국제교류지원팀</td>
            <td>031-750-2686/2682</td>
          </tr>
          <tr>
            <td rowSpan="2">디지털정보처</td>
            <td>정보운영팀</td>
            <td>031-750-4703</td>
          </tr>
          <tr>
            <td>정보인프라팀</td>
            <td>031-750-4703</td>
          </tr>
        </tbody>
      </table>
    </div><Footer/></div>
  );
}
