import React from 'react';
import LogHelper from "../utils/LogHelper.jsx";
import {Form, Input, Button, message} from 'antd';
import {UserOutlined, LockOutlined} from '@ant-design/icons';
import LoginAPICaller from "../utils/LoginAPICaller.jsx";
import URLHelper from "../utils/URLHelper.jsx";

class LoginFrom extends React.Component {

    constructor(props) {
        super(props);
        this.state = {};
        this.handleSubmit = values => {
            LoginAPICaller.login({
                ac: values.ac,
                pw: values.pw,
                successCallback: function (response) {
                    if (response != null && response.code == 200) {
                        message.success(`登录成功即将进行跳转`);
                        URLHelper.openNewPage({finalUrl: URLHelper.getJumpPrefix(), delayTime: 1000});
                    } else {
                        message.error(response.msg);
                    }
                }
            });
        };

        this.handleSubmitFailed = ({values, errorFields, outOfDate}) => {
            console.log('Received values of form: ', values);
            console.log('Received errorFields of form: ', errorFields);
            console.log('Received outOfDate of form: ', outOfDate);
        };
        LogHelper.info({className: "LoginFrom", msg: "constructor----------"});
    }

    static getDerivedStateFromProps(nextProps, nextContext) {
        LogHelper.info({className: "LoginFrom", msg: "getDerivedStateFromProps----------"});
        LogHelper.debug({className: "LoginFrom", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "LoginFrom", tag: "nextContext", msg: nextContext, isJson: true});
        LogHelper.debug({msg: ""});
        return nextProps;
    }

    shouldComponentUpdate(nextProps, nextState, nextContext) {
        LogHelper.info({className: "LoginFrom", msg: "shouldComponentUpdate----------"});
        LogHelper.debug({className: "LoginFrom", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "LoginFrom", tag: "nextState", msg: nextState, isJson: true});
        LogHelper.debug({className: "LoginFrom", tag: "nextContext", msg: nextContext, isJson: true});
        LogHelper.debug({msg: ""});
        return true;
    }

    render() {
        return (
            <Form
                name="normal_login"
                style={{margin: "40px auto 0 auto"}}
                className="login-form"
                initialValues={{remember: true}}
                onFinish={this.handleSubmit}
                onFinishFailed={this.handleSubmitFailed}
            >
                <Form.Item
                    name="ac"
                    rules={[{required: true, message: '请输入账号!'}]}
                >
                    <Input prefix={<UserOutlined className="site-form-item-icon"/>} placeholder="账号"/>
                </Form.Item>
                <Form.Item
                    name="pw"
                    rules={[{required: true, message: '请输入密码!'}]}
                >
                    <Input
                        prefix={<LockOutlined className="site-form-item-icon"/>}
                        type="password"
                        placeholder="密码"
                    />
                </Form.Item>
                <Form.Item>
                    <Button type="primary" htmlType="submit" className="login-form-button">
                        登录
                    </Button>
                </Form.Item>
            </Form>
        );
    }

    componentDidMount() {
        LogHelper.info({className: "LoginFrom", msg: "componentDidMount----------"});
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        LogHelper.info({className: "LoginFrom", msg: "componentDidUpdate----------"});
        LogHelper.debug({className: "LoginFrom", tag: "prevProps", msg: prevProps, isJson: true});
        LogHelper.debug({className: "LoginFrom", tag: "prevState", msg: prevState, isJson: true});
        LogHelper.debug({className: "LoginFrom", tag: "snapshot", msg: snapshot, isJson: true});
        LogHelper.debug({msg: ""});
    }

    componentWillUnmount() {
        LogHelper.info({className: "LoginFrom", msg: "componentWillUnmount----------"});
    }
}

export default LoginFrom;