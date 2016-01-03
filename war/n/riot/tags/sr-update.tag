<sr-update>
    <div class="container">
        <h1>Service Manager - Update</h1><br>

        <form class="form-horizontal" role="form">
            <div class="form-group row">
                <div className="control-group">
                    <label htmlFor="id" className="col-sm-2 control-label">ID:</label>
                    <input type="text" className="col-sm-10 form-control" id="id" defaultValue value={item.id} required readOnly />
                </div>
                <div class="control-group">
                    <label for="title" class="col-sm-2 control-label">Service:</label>
                    <input type="text" class="col-sm-10 form-control" id="title" ng-enter="page.createItem()" required value={item.service} placeholder="Enter any easy to remember service name.">
                </div>
                <div class="control-group">
                    <label for="desc" class="col-sm-2 control-label">Description:</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="desc" ng-enter="page.createItem()" value={item.description}>
                    </div>
                </div>
                <div class="control-group">
                    <label for="endpoint" class="col-sm-2 control-label">URL:</label>
                    <div class="col-sm-10">
                        <input type="url" class="form-control" id="endpoint" ng-enter="page.createItem()" required value={item.endpoint} placeholder="Enter any valid URL e.g. http://www.google.com.">
                    </div>
                </div>
                <div class="control-group">
                    <label for="createItem" class="col-sm-2 control-label"></label>
                    <div class="col-sm-10">
                        <input type="hidden" name="_method" value="POST">
                            <input class="form-control btn btn-primary" id="createItem" type="submit" style="background:#98969E;"
                                   value="Save" ng-click="page.createItem()">
                                <input class="form-control btn" id="cancelItem" value="Cancel" type="button" onclick="location.href='fusrstart.html'">
                    </div>
                </div>
            </div>
        </form>
    </div>
    <script>
    this.item = opts.data;
    this.on('mount', function () {
        console.log("sr-update tag mounted");
        //console.log(this.item);
    });
    </script>
</sr-update>