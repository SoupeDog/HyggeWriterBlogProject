import React from 'react';
import LogHelper from "../utils/LogHelper.jsx";
import {Form, Icon, Input, Button, message} from 'antd';
import APICaller from "../utils/APICaller.jsx";

class LoginFrom extends React.Component {

    constructor(props) {
        super(props);
        this.state = {};
        this.handleSubmit = e => {
            e.preventDefault();
            this.props.form.validateFields((err, values) => {
                if (!err) {
                    APICaller.login({
                        ac: values.ac,
                        pw: values.pw,
                        successCallback: function (response) {
                            if (response != null && response.code == 200) {
                                message.success(`登录成功即将进行跳转`);
                            } else {
                                message.error(response.msg);
                            }
                        }
                    });
                }
            });
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
        const {getFieldDecorator} = this.props.form;
        return (
            <Form style={{margin: "40px auto 0 auto"}} onSubmit={this.handleSubmit} className="login-form">
                <Form.Item>
                    {getFieldDecorator('ac', {
                        rules: [{required: true, message: '请输入账号!'}],
                    })(
                        <Input
                            prefix={<Icon type="user" style={{color: 'rgba(0,0,0,.25)'}}/>}
                            placeholder="账号"
                        />,
                    )}
                </Form.Item>
                <Form.Item>
                    {getFieldDecorator('pw', {
                        rules: [{required: true, message: '请输入密码!'}],
                    })(
                        <Input
                            prefix={<Icon type="lock" style={{color: 'rgba(0,0,0,.25)'}}/>}
                            type="password"
                            placeholder="密码"
                        />,
                    )}
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