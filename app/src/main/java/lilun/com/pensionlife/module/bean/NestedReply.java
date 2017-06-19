package lilun.com.pensionlife.module.bean;

import lilun.com.pensionlife.base.BaseBean;

/**
 * 回复嵌套回复模型
 *
 * @author yk
 *         create at 2017/3/13 15:57
 *         email : yk_developer@163.com
 */
public class NestedReply extends BaseBean {
    public OrganizationReply question;
    public OrganizationReply answer;

    public OrganizationReply getQuestion() {
        return question;
    }

    public NestedReply setQuestion(OrganizationReply question) {
        this.question = question;
        return this;
    }

    public OrganizationReply getAnswer() {
        return answer;
    }

    public NestedReply setAnswer(OrganizationReply answer) {
        this.answer = answer;
        return this;
    }

    public NestedReply() {
    }

    public NestedReply(OrganizationReply question, OrganizationReply answer) {
        this.question = question;
        this.answer = answer;
    }
}
