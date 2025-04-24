import { BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import Main from "./main/Main";
import UserLogin from "./login/UserLogin";
import Signup from "./signup/Signup";
import Info from "./info/Info";
import Intro from "./intro/Intro";
import Inside from "./inside/Inside";
import Mypage from "./mypage/Mypage";
import Write from "./write/Write";
import UserManage from "./manage/UserManage";
import PostManage from "./manage/PostManage";
import AdminInside from "./inside/AdminInside";
import Edit from "./inside/Edit";
import Chatbot from "./chatbot/Chatbot";
import "./App.css";

function App() {
return (
    <Router>
      <div></div>
          <Routes>
              <Route path="/usermanage" Component={UserManage} />
              <Route path="/postmanage" Component={PostManage} />
              <Route path="/admininside/:postId" Component={AdminInside} />
              <Route path="/edit/:postId" Component={Edit} />
              <Route path="/chatbot" Component={Chatbot} />
              <Route path="/intro" Component={Intro} />
              <Route path="/info" Component={Info} />
              <Route path="/" Component={Intro} />
              <Route path="/vos" Component={Main} />
              <Route path="/inside/:postId" Component={Inside} />
              <Route path="/userlogin" Component={UserLogin} />
              <Route path="/signup" Component={Signup} />
              <Route path="/mypage" Component={Mypage} />
              <Route path="/write" Component={Write} />
          </Routes>
    </Router>
  );
}
export default App;
