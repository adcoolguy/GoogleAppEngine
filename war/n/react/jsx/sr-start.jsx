var key = localStorage.getItem('userJWTToken');

const ACTIVE = { color: 'red' };

var SRStart = React.createClass({
  //mixins: [RouterMixin],
  //routes: {
  //  '/': 'start',
  //  '/fusrcreate': 'fusrcreate',
  //  '/message/:text': 'message'
  //},
  //childContextTypes: {
  //  router: React.PropTypes.func
  //},
  //getChildContext () {
  //  return {
  //    router: router
  //  }
  //},
  propTypes: {
    $state: React.PropTypes.func.required
  },
  //start: function() {
  getInitialState: function() {
    var component = this;
    var items = {};
    window.swagger = new SwaggerClient({
      url: location.origin + "/swagger/swagger.json",
      success: function() {
        swagger.sr.all({},{responseContentType: 'application/json'}, function(data) {
          //document.getElementById("mydata").innerHTML = JSON.stringify(data.obj);
          component.state.items = data.obj.content;
          ReactDOM.render(<SRStart $state={component.state.$state} items={ data.obj.content }/>, document.getElementById('sr-start'));
          $('#sr-start-table').stacktable();
        });
      },
      authorizations : {
        someHeaderAuth: new SwaggerClient.ApiKeyAuthorization('Authorization', "Bearer " + key, 'header')
      }
    });

    return items;
  },
  goUpdate: function(id, e) {
    var obj = {
      id: id
    }
    //console.log('sr-start.jsx goUpdate: ', id, this.state);
    this.state.$state.go('update', {obj: obj});
  },
  render: function() {
    var indents = [];
    var objects = this.state.items;
    this.state.$state=this.props.$state;
    //console.log(this.props);
    if(objects) {
      for (var i = 0; i < objects.length; i++) {
        var boundItemClick = this.goUpdate.bind(this, objects[i].id);

        indents.push(<tr key={i}>

          <td><a onClick={boundItemClick}>{objects[i].id}</a></td>
          <td><a target="_new" href={location.origin+'/go/'+objects[i].service+'?incog=true'}>{objects[i].service}</a>
          </td>
          <td dangerouslySetInnerHTML={{__html: objects[i].description}}/>
          <td>{objects[i].url}</td>
          <td>{objects[i].lastAccessed}</td>
          <td>{objects[i].lastUpdated}</td>
          <td><a target="_new" href={objects[i].endpoint}>{objects[i].endpoint}</a></td>
          <td>{objects[i].hit}</td>
        </tr>);
      }
    }
    return (
        <div>
          <h4><a href="#/create">Create New</a></h4>
          {/* <h4><Link {...this.props} to="/fusrcreate" activeStyle={ACTIVE}>Create New</Link></h4> */}
          <div className="table-responsive">
            <table id="sr-start-table" className="table table-striped table-bordered">
              <thead>
              <tr>
                <td>ID</td>
                <td>SERV</td>
                <td>DESCRIPTION</td>
                <td>S URL</td>
                <td>VIS</td>
                <td>UPT</td>
                <td>ENDPOINT</td>
                <td>HIT</td>
              </tr>
              </thead>
              <tbody>
              {indents}
              </tbody>
            </table>
          </div>
        </div>
    );
  }
  //,
  //render: function() {
    //return this.renderCurrentRoute();
  //}
  //,
  //fusrcreate: function() {
  //  return <SRCreate />;
  //},
  //message: function(text) {
  //  return <div>{text}</div>;
  //},
  //notFound: function(path) {
  //  return <div class="not-found">Page Not Found: {path}</div>;
  //}
});

ReactDOM.render(<SRStart $state />, document.getElementById('sr-start'));