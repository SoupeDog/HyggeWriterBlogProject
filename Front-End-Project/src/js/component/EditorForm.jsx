import React from 'react';
import LogHelper from "../utils/LogHelper.jsx";
import {Button, Form, Input, message, Radio, Select} from "antd";
// import $ from 'jquery'
import APICaller from "../utils/APICaller.jsx";
import URLHelper from "../utils/URLHelper.jsx";
import Vditor from "vditor";
import PropertiesHelper from "../utils/PropertiesHelper.jsx";

const {TextArea} = Input;

class EditorForm extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            hasError: false,
            fileInfo: [],
            articleCategoryInfo: []
        };

        this.handleSubmit = values => {
            if (values.actionType == "修改文章") {
                APICaller.updateArticle({
                    articleNo: URLHelper.getQueryString("articleNo"),
                    boardNo: values.boardNo,
                    articleCategoryNo: values.articleCategoryNo,
                    title: values.title,
                    summary: values.summary,
                    content: values.content,
                    bgi: values.bgi,
                    bgmSrc: values.bgmSrc,
                    bgmCover: values.bgmCover,
                    bgmType: values.bgmType,
                    bgmName: values.bgmName,
                    bgmArtist: values.bgmArtist,
                    bgmLrc: values.bgmLrc,
                    ts: new Date().getTime(),
                    successCallback: function (response) {
                        if (response != null && response.code == 200) {
                            message.success(`修改成功`);
                        } else {
                            message.error(response.msg);
                        }
                    }
                });
            }

            if (values.actionType == "添加文章") {
                APICaller.addArticle({
                    boardNo: values.boardNo,
                    articleCategoryNo: values.articleCategoryNo,
                    title: values.title,
                    summary: values.summary,
                    content: values.content,
                    bgi: values.bgi,
                    bgmSrc: values.bgmSrc,
                    bgmCover: values.bgmCover,
                    bgmType: values.bgmType,
                    bgmName: values.bgmName,
                    bgmArtist: values.bgmArtist,
                    bgmLrc: values.bgmLrc,
                    successCallback: function (response) {
                        if (response != null && response.code == 200) {
                            message.success(`添加成功`);
                        } else {
                            message.error(response.msg);
                        }
                    }
                });
            }
        };

        this.handleSubmitFailed = ({values, errorFields, outOfDate}) => {
            console.log('Received values of form: ', values);
            console.log('Received errorFields of form: ', errorFields);
            console.log('Received outOfDate of form: ', outOfDate);
        };

        // 查询文章信息
        this.queryArticleForEditor = function () {
            let _react = this;
            let currentArticleNo = URLHelper.getQueryString("articleNo");
            if (currentArticleNo != null) {
                APICaller.queryArticle({
                    articleNo: currentArticleNo,
                    successCallback: function (response) {
                        // console.log(response);
                        if (response != null && response.code == 200) {
                            let article = response.data;
                            let finalBgi = "";
                            if (article.properties.bgi != null) {
                                finalBgi = article.properties.bgi.substring(URLHelper.getStaticPrefix().length, article.properties.bgi.length);
                            }
                            let finalValues = {
                                boardNo: article.boardNo,
                                articleCategoryNo: article.articleCategoryNo,
                                title: article.title,
                                summary: article.summary,
                                content: article.content,
                                bgi: finalBgi
                            };

                            if (article.properties.bgmConfig.src != null) {
                                finalValues.bgmSrc = article.properties.bgmConfig.src;
                            } else {
                                finalValues.bgmType = "0";
                            }
                            if (article.properties.bgmConfig.bgmType != null) {
                                finalValues.bgmType = article.properties.bgmConfig.bgmType + "";
                            }
                            if (article.properties.bgmConfig.cover != null) {
                                finalValues.bgmCover = article.properties.bgmConfig.cover;
                            }
                            if (article.properties.bgmConfig.name != null) {
                                finalValues.bgmName = article.properties.bgmConfig.name;
                            }
                            if (article.properties.bgmConfig.artist != null) {
                                finalValues.bgmArtist = article.properties.bgmConfig.artist;
                            }
                            if (article.properties.bgmConfig.lrc != null) {
                                finalValues.bgmLrc = article.properties.bgmConfig.lrc;
                            }
                            _react.state.form.setFieldsValue(finalValues);
                        } else {
                            message.error(response.msg);
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
        this.queryConfigPropertiesForEditor = function () {
            let _react = this;
            APICaller.queryFileInfo({
                successCallback: function (response) {
                    if (response != null && response.code == 200) {
                        _react.setState({fileInfo: response.data});
                    } else {
                        message.error(response.msg);
                    }

                }
            });
            APICaller.queryArticleCategoryAll({
                successCallback: function (response) {
                    if (response != null && response.code == 200) {
                        _react.setState({articleCategoryInfo: response.data});
                    } else {
                        message.error(response.msg);
                    }
                }
            });
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
        let _react = this;
        if (_react.state.hasError) {
            return <h1>Something went wrong.</h1>;
        } else {
            return (
                <Form ref={(form) => {
                    if (_react.state.form == null) {
                        _react.setState({form: form})
                    }
                }}
                      style={{margin: "40px auto 0 auto"}}
                      className="login-form"
                      initialValues={{remember: true}}
                      onFinish={_react.handleSubmit}
                      onFinishFailed={_react.handleSubmitFailed}
                      form={_react.state.form}>
                    <Form.Item label="板块类型" name="boardNo" rules={[{required: true, message: '请选择板块类型'}]}>
                        <Radio.Group>
                            <Radio value="ae81801808f8485384b66da526169da2">技术板块</Radio>
                            <Radio value="b9cc9df574b448b088303f8056198d91">非技术板块</Radio>
                        </Radio.Group>
                    </Form.Item>
                    <Form.Item label="标题" name="title" rules={[{required: true, message: '请选择标题'}]}>
                        <Input placeholder="请输入标题"/>
                    </Form.Item>
                    <Form.Item label="文章类别" name="articleCategoryNo"
                               rules={[{required: true, message: '请选择文章类别'}]}>
                        <Select
                            showSearch
                            placeholder="请选择文章类别"
                            optionFilterProp="children"
                            filterOption={(input, option) => {
                                let currentItem = option.props.children;
                                if (currentItem != null) {
                                    console.log(currentItem + "----" + PropertiesHelper.isStringNotNull(currentItem))
                                    return currentItem.toLowerCase().indexOf(input.toLowerCase()) >= 0
                                } else {
                                    return false;
                                }
                            }
                            }
                        >
                            {
                                _react.state.articleCategoryInfo.map((articleCategoryItem, index) => {
                                    return (
                                        <Select.Option key={index}
                                                       value={articleCategoryItem.articleCategoryNo}>{articleCategoryItem.articleCategoryName + "-" + articleCategoryItem.description}</Select.Option>
                                    )
                                })
                            }
                        </Select>
                    </Form.Item>
                    <Form.Item label="背景图片" name="bgi" rules={[{required: true, message: '请选择背景图片'}]}>
                        <Select
                            showSearch
                            placeholder="请选择背景图片"
                            optionFilterProp="children"
                            filterOption={(input, option) => {
                                let currentItem = option.props.children;
                                if (currentItem != null) {
                                    console.log(currentItem + "----" + PropertiesHelper.isStringNotNull(currentItem))
                                    return currentItem.toLowerCase().indexOf(input.toLowerCase()) >= 0
                                } else {
                                    return false;
                                }
                            }
                            }
                        >
                            {
                                _react.state.fileInfo.map((fileItem, index) => {
                                    return (
                                        <Select.Option key={index}
                                                       value={fileItem.fileName}>{fileItem.fileName}</Select.Option>
                                    )
                                })
                            }
                        </Select>
                    </Form.Item>
                    <Form.Item label="背景音乐类型" name="bgmType">
                        <Radio.Group>
                            <Radio value="0">无音乐</Radio>
                            <Radio value="1">网易云音乐外链</Radio>
                            <Radio value="2">音频文件直连</Radio>
                        </Radio.Group>
                    </Form.Item>
                    <Form.Item label="背景音乐资源路径" name="bgmSrc"
                               rules={[{required: false, message: '请输入背景音乐资源路径'}]}>
                        <Input placeholder="请输入背景音乐资源路径"/>
                    </Form.Item>
                    <Form.Item label="背景音乐资源封面" name="bgmCover"
                               rules={[{required: false, message: '请输入背景音乐资源封面'}]}>
                        <Input placeholder="请输入背景音乐资源封面"/>
                    </Form.Item>
                    <Form.Item label="背景音乐名称" name="bgmName"
                               rules={[{required: false, message: '请输入背景音乐名称'}]}>
                        <Input placeholder="请输入背景音乐名称"/>
                    </Form.Item>
                    <Form.Item label="背景音乐艺术家" name="bgmArtist"
                               rules={[{required: false, message: '请输入背景音乐艺术家'}]}>
                        <Input placeholder="请输入背景音乐艺术家"/>
                    </Form.Item>
                    <Form.Item label="背景音乐歌词文件" name="bgmLrc"
                               rules={[{required: false, message: '请输背景音乐歌词文件路径'}]}>
                        <Input placeholder="请输入背景音乐歌词文件路径"/>
                    </Form.Item>
                    <Form.Item label="摘要" name="summary" rules={[{required: false, message: '请输入摘要'}]}>
                        <TextArea
                            placeholder="摘要"
                            autosize={{minRows: 2, maxRows: 200}}
                        />
                    </Form.Item>
                    <Form.Item label="内容" name="content" rules={[{required: false, message: '请输入内容'}]}>
                        <TextArea
                            onBlur={() => {
                                Vditor.preview(document.getElementById('preview'),
                                    $("#content").text(), {
                                        className: 'preview vditor-reset vditor-reset--anchor',
                                        anchor: false,
                                        after:()=>{
                                            // 清除代码高度限制
                                            $("#preview").find("code").each(function () {
                                                let currentTarget = $(this);
                                                currentTarget.css("max-height", "");
                                            });
                                        }
                                    });
                            }}
                            placeholder="文章内容"
                            autosize={{minRows: 4, maxRows: 400}}
                        />
                    </Form.Item>
                    <Form.Item label="执行类型" name="actionType" rules={[{required: true, message: '请选择执行类型'}]}>
                        <Radio.Group>
                            <Radio value="添加文章">添加文章</Radio>
                            <Radio value="修改文章">修改文章</Radio>
                        </Radio.Group>
                    </Form.Item>
                    <Form.Item>
                        <Button type="primary" htmlType="button" className="editor-form-button"
                                onClick={_react.queryArticleForEditor}>
                            查询文章
                        </Button>
                        <Button type="primary" htmlType="button" className="editor-form-button"
                                onClick={() => {
                                    Vditor.preview(document.getElementById('preview'),
                                        $("#content").text(), {
                                            className: 'preview vditor-reset vditor-reset--anchor',
                                            anchor: false,
                                            after:()=>{
                                                // 清除代码高度限制
                                                $("#preview").find("code").each(function () {
                                                    let currentTarget = $(this);
                                                    currentTarget.css("max-height", "");
                                                });
                                            }
                                        });
                                }}>
                            预览文章
                        </Button>
                        <Button type="primary" htmlType="button" className="editor-form-button"
                                onClick={_react.queryConfigPropertiesForEditor}>
                            拉取配置信息
                        </Button>
                        <Button type="danger" htmlType="submit" className="editor-form-button">
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
        return null;
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
