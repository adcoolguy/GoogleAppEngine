module.exports = {
        'DalekJS Hi Res Test': function (test) {
                  test.open('http://chudoo.tv').waitForElement('#notice', 10000).assert.chain().title('Cool App').exists('#notice','For Personal Use Only').end().wait(3000).click('#username').type('#username','j').wait(2000).click('#password').type('#password','j').sendKeys('#password','\uE007').waitForElement('#myaccount', 10000).wait(5000).screenshot().done();
        }
};
