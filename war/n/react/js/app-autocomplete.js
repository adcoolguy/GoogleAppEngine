"use strict";

var styles = {
    item: {
        padding: '2px 6px',
        cursor: 'default'
    },

    highlightedItem: {
        color: 'white',
        background: 'hsl(250, 50%, 50%)',
        padding: '2px 6px',
        cursor: 'default'
    },

    menu: {
        border: 'solid 1px #ccc'
    }
};

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var AppAutocomplete = React.createClass({
    displayName: 'App',

    getInitialState: function getInitialState() {
        return {
            unitedStates: getStates(),
            loading: false
        };
    },

    render: function render() {
        var _this = this;

        return React.createElement(
            'div',
            null,
            React.createElement(Autocomplete, {
                items: this.state.unitedStates,
                getItemValue: function (item) {
                    return item.name;
                },
                onSelect: function (value) {
                    var index = value.indexOf('-');
                    var index2 = value.indexOf('[');
                    var service = value.substr(0, index);
                    var appId = value.substr(index+1, index2-index-2);
                    console.log('app-autocomplete.js onSelect(): value [' + value + '] service [' + service + '] appId [' + appId + ']');
                    _this.popup = React.createElement(AppIframe, {url: 'https://'+appId+'.appspot.com/go/'+service, width: '60%', height: '60%'});
                    ReactDOM.render(_this.popup, document.getElementById('popup'));

                    return _this.setState({ unitedStates: [] });
                },
                onChange: function (event, value) {
                    if(typeof _this.popup !== 'undefined') {
                        _this.popup = React.createElement(AppIframe, {url: '', hide: true});
                        ReactDOM.render(_this.popup, document.getElementById('popup'));
                    }
//                    console.log('app-autocomplete.js onChange(): value [' + value + ']');
                    _this.setState({ loading: true });
                    fakeRequest(value, function (items) {
                        _this.setState({ unitedStates: items, loading: false });
                    });
                },
                renderItem: function (item, isHighlighted) {
                    return React.createElement(
                        'div',
                        {
                            style: isHighlighted ? styles.highlightedItem : styles.item,
                            key: item.abbr,
                            id: item.abbr
                        },
                        item.name
                    );
                },
                renderMenu: function (items, value, style) {
                    return React.createElement(
                        'div',
                        { style: _extends({}, styles.menu, style) },
                        value === '' ? React.createElement(
                            'div',
                            { style: { padding: 6 } },
                            'Enter any search keyword'
                        ) : _this.state.loading ? React.createElement(
                            'div',
                            { style: { padding: 6 } },
                            'Loading...'
                        ) : items.length === 0 ? React.createElement(
                            'div',
                            { style: { padding: 6 } },
                            'No matches for ',
                            value
                        ) : _this.renderItems(items)
                    );
                }
            })
        );
    },

    renderItems: function renderItems(items) {
        //console.log(items);
        return items.map(function (item, index) {
            var text = item.props.children;
            if (index === 0 || items[index - 1].props.children.charAt(0) !== text.charAt(0)) {
                var style = {
                    background: '#eee',
                    color: '#454545',
                    padding: '2px 6px',
                    fontWeight: 'bold'
                };
                return [React.createElement(
                    'div',
                    { style: style },
                    text.charAt(0)
                ), item];
            } else {
                return item;
            }
        });
    }
});

var searchedResults = [];
var arr = [];
var r = {};
function getES(term) {
    if(typeof term !== 'undefined') {
        $.get('https://es-n3t.rhcloud.com/service_registry/_search?size=100&pretty&q='+term, function(result) {
            if (typeof result !== 'undefined') {
                searchedResults = [];
                arr = [];
                r = {};
                //console.log('app-autocomplete.js getES(): term [' + term + '] result [' + result.hits.total + ']');
                r = result.hits.hits;
                //console.log(r);
                var h;
                for (var i = 0; i < r.length; i++) {
                    h = r[i];
                    arr.push(h._source);
                }
                searchedResults = arr.reduce(function (all, item, index) {
                    all.push({abbr: item.service.substr(0, 2), name: item.service + " [" + item.summary + "]"});
                    return all;
                }, []);
            }
        });
    }
}

function getStates(value) {
    getES(value);

    return searchedResults;
}

function matchStateToTerm(state, value) {
    return (
        state.name.toLowerCase().indexOf(value.toLowerCase()) !== -1 ||
        state.abbr.toLowerCase().indexOf(value.toLowerCase()) !== -1
    )
}

function sortStates(a, b, value) {
    return (
        a.name.toLowerCase().indexOf(value.toLowerCase()) >
        b.name.toLowerCase().indexOf(value.toLowerCase()) ? 1 : -1
    )
}

function fakeRequest(value, cb) {
    if (value === '') return getStates(value);
    var items = getStates(value).filter(function (state) {
        return matchStateToTerm(state, value);
    });
    setTimeout(function () {
        cb(items);
    }, 100);
}

//ReactDOM.render(React.createElement(AppAutocomplete, null), document.getElementById('app-autocomplete'));