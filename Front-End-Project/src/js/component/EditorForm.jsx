import React from 'react';
import LogHelper from "../utils/LogHelper.jsx";
import {Button, Form, Radio, Select, Input} from "antd";
import $ from 'jquery'
import APICaller from "../utils/APICaller.jsx";
import URLHelper from "../utils/URLHelper.jsx";
import ReactDOM from "react-dom";
import BrowseContainer from "../pages/BrowseContainer.jsx";

const {TextArea} = Input;

class EditorForm extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            hasError: false,
            fileInfo: [{"fileName": "搞事情"}]
        };

        this.handleSubmit = e => {
            e.preventDefault();
            this.props.form.validateFields((err, values) => {
                if (!err) {
                    console.log(values)
                    // ConfigAPI.updateConfig({
                    //     config: {properties: JSON.stringify(values)},
                    //     successCallback: function (response) {
                    //         UserAPI.defaultSuccessCallback(response);
                    //         if (response != null && response.code == 200) {
                    //             message.success(`修改成功`);
                    //         } else {
                    //             message.error(response.msg);
                    //         }
                    //     },
                    //     errorCallback: function () {
                    //     },
                    //     timeOutCallback: function () {
                    //     },
                    //     finallyCallback: function () {
                    //     }
                    // });
                }
            });
        };

        // 查询文章信息
        this.queryArticleForEditor = function () {
            let _react = this;
            let currentArticleNo = URLHelper.getQueryString("articleNo");
            if (currentArticleNo != null) {
                APICaller.queryArticle({
                    articleNo: currentArticleNo,
                    successCallback: function (response) {
                        console.log(response);
                        if (response.code == 200) {
                            let article = response.data;

                            let finalValues = {
                                boardNo: article.boardNo,
                                articleCategoryNo: article.articleCategoryNo,
                                summary: article.summary,
                                content: article.content,
                                bgi: article.properties.bgi
                            };

                            if (article.properties.bgmConfig.src != null) {
                                finalValues.bgmSrc = article.properties.bgmConfig.src;
                            }
                            if (article.properties.bgmConfig.bgmType != null) {
                                finalValues.bgmType = article.properties.bgmConfig.bgmType + "";
                            }
                            _react.props.form.setFieldsValue(finalValues);

                        } else {
                            alert("请求失败")
                        }
                    },
                    errorCallback: function (response) {
                        console.log(response);
                        alert("Error")
                    }
                });
            }
        }.bind(this);

        // 查询文件信息
        this.queryConfigPropertiesForEditor=function(){
            APICaller.querySummaryByPage()


        }.bind(this);

        LogHelper.info({className: "EditorForm", msg: "constructor----------"});
    }

    static getDerivedStateFromProps(nextProps, nextContext) {
        LogHelper.info({className: "EditorForm", msg: "getDerivedStateFromProps----------"});
        LogHelper.debug({className: "EditorForm", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "EditorForm", tag: "nextContext", msg: nextContext, isJson: true});
        LogHelper.debug({msg: ""});
        return nextProps;
    }

    render() {
        const {getFieldDecorator} = this.props.form;
        if (this.state.hasError) {
            return <h1>Something went wrong.</h1>;
        } else {
            return (
                <Form style={{margin: "40px auto 0 auto"}} onSubmit={this.handleSubmit} className="login-form">
                    <Form.Item label="板块类型">
                        {getFieldDecorator('boardNo', {
                            rules: [{required: true, message: '请选择板块类型'}],
                        })(
                            <Radio.Group>
                                <Radio value="ae81801808f8485384b66da526169da2">技术板块</Radio>
                                <Radio value="b9cc9df574b448b088303f8056198d91">非技术板块</Radio>
                            </Radio.Group>,
                        )}
                    </Form.Item>
                    <Form.Item label="文章类别">
                        {getFieldDecorator('articleCategoryNo', {
                            rules: [{required: true, message: '请选择文章类别'}],
                        })(
                            <Select
                                showSearch
                                placeholder="请选择文章类别"
                                optionFilterProp="children"
                                filterOption={(input, option) =>
                                    option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                                }
                            >
                                {
                                    this.state.fileInfo.map((fileItem, index) => {
                                        return (
                                            <Select.Option key={index}
                                                           value={fileItem.fileName}>{fileItem.fileName}</Select.Option>
                                        )
                                    })
                                }
                            </Select>,
                        )}
                    </Form.Item>
                    <Form.Item label="背景图片">
                        {getFieldDecorator('bgi', {
                            rules: [{required: true, message: '请选择背景图片'}],
                        })(
                            <Select
                                showSearch
                                placeholder="请选择背景图片"
                                optionFilterProp="children"
                                filterOption={(input, option) =>
                                    option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                                }
                            >
                                {
                                    this.state.fileInfo.map((fileItem, index) => {
                                        return (
                                            <Select.Option key={index}
                                                           value={fileItem.fileName}>{fileItem.fileName}</Select.Option>
                                        )
                                    })
                                }
                            </Select>
                        )}
                    </Form.Item>
                    <Form.Item label="背景音乐类型">
                        {getFieldDecorator('bgmType')(
                            <Radio.Group>
                                <Radio value="1">网易云音乐外链</Radio>
                                <Radio value="2">音频文件直连</Radio>
                            </Radio.Group>,
                        )}
                    </Form.Item>

                    <Form.Item label="背景音乐资源路径">
                        {getFieldDecorator('bgmSrc', {
                            rules: [{required: false, message: '请选择背景音乐资源路径'}],
                        })(
                            <Select
                                showSearch
                                placeholder="请选择背景音乐资源路径"
                                optionFilterProp="children"
                                filterOption={(input, option) =>
                                    option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                                }
                            >
                                {
                                    this.state.fileInfo.map((fileItem, index) => {
                                        return (
                                            <Select.Option key={index}
                                                           value={fileItem.fileName}>{fileItem.fileName}</Select.Option>
                                        )
                                    })
                                }
                            </Select>
                        )}
                    </Form.Item>

                    <Form.Item label="摘要">
                        {getFieldDecorator('summary', {
                            rules: [{required: false, message: '请输入摘要'}],
                        })(
                            <TextArea
                                placeholder="摘要"
                                autosize={{minRows: 2, maxRows: 200}}
                            />
                        )}
                    </Form.Item>

                    <Form.Item label="内容">
                        {getFieldDecorator('content', {
                            rules: [{required: false, message: '请输入内容'}],
                        })(
                            <TextArea
                                placeholder="文章内容"
                                autosize={{minRows: 4, maxRows: 400}}
                            />
                        )}
                    </Form.Item>
                    <Form.Item label="执行类型">
                        {getFieldDecorator('actionType')(
                            <Radio.Group>
                                <Radio value="添加文章">添加文章</Radio>
                                <Radio value="修改文章">修改文章</Radio>
                            </Radio.Group>,
                        )}
                    </Form.Item>
                    <Form.Item>
                        <Button type="primary" htmlType="button" className="login-form-button"
                                onClick={this.queryArticleForEditor}>
                            查询文章
                        </Button>
                        <Button type="primary" htmlType="button" className="login-form-button"
                                onClick={this.queryConfigPropertiesForEditor}>
                            拉取配置信息
                        </Button>
                        <Button type="danger" htmlType="submit" className="login-form-button">
                            执行
                        </Button>
                    </Form.Item>
                </Form>
            );
        }
    }

    shouldComponentUpdate(nextProps, nextState, nextContext) {
        LogHelper.info({className: "EditorForm", msg: "shouldComponentUpdate----------"});
        LogHelper.debug({className: "EditorForm", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "EditorForm", tag: "nextState", msg: nextState, isJson: true});
        LogHelper.debug({className: "EditorForm", tag: "nextContext", msg: nextContext, isJson: true});
        LogHelper.debug({msg: ""});
        return true;
    }

    componentDidMount() {
        LogHelper.info({className: "EditorForm", msg: "componentDidMount----------"});
        LogHelper.debug({msg: ""});
    }

    getSnapshotBeforeUpdate(prevProps, prevState) {
        LogHelper.info({className: "EditorForm", msg: "getSnapshotBeforeUpdate----------"});
        LogHelper.debug({className: "EditorForm", tag: "prevProps", msg: prevProps, isJson: true});
        LogHelper.debug({className: "EditorForm", tag: "prevState", msg: prevState, isJson: true});
        LogHelper.debug({msg: ""});
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        LogHelper.info({className: "EditorForm", msg: "componentDidUpdate----------"});
        LogHelper.debug({className: "EditorForm", tag: "prevProps", msg: prevProps, isJson: true});
        LogHelper.debug({className: "EditorForm", tag: "prevState", msg: prevState, isJson: true});
        LogHelper.debug({className: "EditorForm", tag: "snapshot", msg: snapshot, isJson: true});
        LogHelper.debug({msg: ""});
    }

    static getDerivedStateFromError(error) {
        LogHelper.info({className: "EditorForm", msg: "getDerivedStateFromError----------"});
        LogHelper.error({className: "EditorForm", tag: "error", msg: error, isJson: true});
        LogHelper.error({msg: ""});
        return {hasError: true};
    }

    componentDidCatch(error, info) {
        LogHelper.info({className: "EditorForm", msg: "componentDidCatch----------"});
        LogHelper.debug({className: "EditorForm", tag: "error", msg: error, isJson: true});
        LogHelper.debug({className: "EditorForm", tag: "info", msg: info, isJson: true});
        LogHelper.debug({msg: ""});
    }
}

export default EditorForm;
