var SRCreate = React.createClass({
    render: function() {
        return (
            <SRUpdate title="Create" $state={this.props.$state} createItemCallback={this.createItem} />
        )
    },
    createItem: function(component) {
        var key = localStorage.getItem('userJWTToken');
        window.swagger = new SwaggerClient({
            url: location.origin + "/swagger/swagger.json",
            success: function() {
                //console.log('sr-create.jsx createItem(): ' + component.state.service);
                //swagger.sr.id({id: 'h'}, {responseContentType: 'application/json'}, function (data) {
                swagger.sr.existsSR_service({service: component.state.service}, {responseContentType: 'application/json'}, function(data) {
                    console.log(data.obj);
                    if(data.obj && typeof data !== 'undefined' && typeof data.obj !== 'undefined') {
                        if(data.obj && data.obj.service != component.state.service) {
                            //hmm...
                        } else {
                            status = 'service [' + component.state.service + '] exists!';
                            alert(status);
                        }
                    } else {
                        console.log('createItem() unknown error!');
                        //if(data.obj && data.obj.service != component.state.service) {
                        component.saveNow(component);
                        //}
                    }
                });
            },
            authorizations : {
                someHeaderAuth: new SwaggerClient.ApiKeyAuthorization('Authorization', "Bearer " + key, 'header')
            }
        });
        //console.log('SRCreate invoked');
    }
});
