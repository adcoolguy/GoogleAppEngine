"use strict";

var styles = {
};

var AppAutocomplete = React.createClass({
    render: function() {
        var component = this;
        return React.createElement(
            "div",
            null,
            React.createElement(Autocomplete, {
                initialValue: "Ma",
                items: getStates(),
                getItemValue: function (item) {
                    return item.name;
                },
                shouldItemRender: matchStateToTerm,
                sortItems: sortStates,
                renderItem: function (item, isHighlighted) {
                    return React.createElement(
                        "div",
                        {
                            style: isHighlighted ? styles.highlightedItem : styles.item,
                            key: item.abbr
                        },
                        item.name
                    );
                }
            })
        );
    }
});

function getStates() {
    return [
        {abbr: "AL", name: "Alabama"},
        {abbr: "AK", name: "Alaska"},
        {abbr: "AZ", name: "Arizona"},
        {abbr: "AR", name: "Arkansas"},
        {abbr: "CA", name: "California"},
        {abbr: "CO", name: "Colorado"},
        {abbr: "CT", name: "Connecticut"},
        {abbr: "DE", name: "Delaware"},
        {abbr: "FL", name: "Florida"},
        {abbr: "GA", name: "Georgia"},
        {abbr: "HI", name: "Hawaii"},
        {abbr: "ID", name: "Idaho"},
        {abbr: "IL", name: "Illinois"},
        {abbr: "IN", name: "Indiana"},
        {abbr: "IA", name: "Iowa"},
        {abbr: "KS", name: "Kansas"},
        {abbr: "KY", name: "Kentucky"},
        {abbr: "LA", name: "Louisiana"},
        {abbr: "ME", name: "Maine"},
        {abbr: "MD", name: "Maryland"},
        {abbr: "MA", name: "Massachusetts"},
        {abbr: "MI", name: "Michigan"},
        {abbr: "MN", name: "Minnesota"},
        {abbr: "MS", name: "Mississippi"},
        {abbr: "MO", name: "Missouri"},
        {abbr: "MT", name: "Montana"},
        {abbr: "NE", name: "Nebraska"},
        {abbr: "NV", name: "Nevada"},
        {abbr: "NH", name: "New Hampshire"},
        {abbr: "NJ", name: "New Jersey"},
        {abbr: "NM", name: "New Mexico"},
        {abbr: "NY", name: "New York"},
        {abbr: "NC", name: "North Carolina"},
        {abbr: "ND", name: "North Dakota"},
        {abbr: "OH", name: "Ohio"},
        {abbr: "OK", name: "Oklahoma"},
        {abbr: "OR", name: "Oregon"},
        {abbr: "PA", name: "Pennsylvania"},
        {abbr: "RI", name: "Rhode Island"},
        {abbr: "SC", name: "South Carolina"},
        {abbr: "SD", name: "South Dakota"},
        {abbr: "TN", name: "Tennessee"},
        {abbr: "TX", name: "Texas"},
        {abbr: "UT", name: "Utah"},
        {abbr: "VT", name: "Vermont"},
        {abbr: "VA", name: "Virginia"},
        {abbr: "WA", name: "Washington"},
        {abbr: "WV", name: "West Virginia"},
        {abbr: "WI", name: "Wisconsin"},
        {abbr: "WY", name: "Wyoming"}
    ];
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

//ReactDOM.render(React.createElement(AppAutocomplete, null), document.getElementById('app-autocomplete'));