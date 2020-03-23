import '../css/default.css';
import '../css/editor.less';

import React from 'react';
import ReactDOM from 'react-dom';
import 'antd/dist/antd.less';
import EditorContainer from "./pages/EditorContainer.jsx";

ReactDOM.render(<EditorContainer/>, document.getElementById('root'));