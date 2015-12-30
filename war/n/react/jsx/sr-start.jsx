window.swagger = new SwaggerClient({
  url: location.origin + "/swagger/swagger.json",
  success: function() {
    swagger.sr.all({},{responseContentType: 'application/json'}, function(data) {
      //document.getElementById("mydata").innerHTML = JSON.stringify(data.obj);
      React.render(<SRStart list={ data.obj.content }/>, document.getElementById('sr-start'));
    });
  }
});
var SRStart = React.createClass({
  render: function() {
    var indents = [];
    var objects = this.props.list;
    //console.log(this.props);
    for (var i=0; i < objects.length; i++) {
      indents.push(<tr key={i}><td><a href="fusrupdate.html?id={id}">{objects[i].id}</a></td><td>{objects[i].service}</td><td>{objects[i].description}</td><td>{objects[i].url}</td><td>{objects[i].lastAccessed}</td><td>{objects[i].lastUpdated}</td><td>{objects[i].endpoint}</td><td>{objects[i].hit}</td></tr>);
    }
    return (
        <div className="table-responsive">
          <table className="table table-striped table-bordered">
            <thead>
            <tr><td>ID</td><td>SERV</td><td>DESCRIPTION</td><td>S URL</td><td>VIS</td><td>UPT</td><td>ENDPOINT</td><td>HIT</td></tr>
            </thead>
            <tbody>
              {indents}
            </tbody>
          </table>
        </div>
    );
  }
});

//React.render(<SRStart list={ data.content }/>, document.getElementById('sr-start'))