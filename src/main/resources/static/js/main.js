$(document).ready(function(){
    queryNetworkNodes();
});

function queryNetworkNodes(){
    $('#cardsPage').empty();

    $.ajax({
        type: "GET",
        url: "http://localhost:8081/api-v1/nodes",
        success: function(response){
            for(let i=0; i < response.length; i++){
                $('#cardsPage').append(newCard(response[i].id, response[i].node_name,response[i].node_location, response[i].latitude, response[i].longitude));
            }
        },
        error: function() {
            createToastMsg("Failed to load network nodes", "red")
        }
    });
}


function handleAdding(){
    var networkName = $('#newNetworkName').val();
    var longitude = $('#newLongitude').val();
    var latitude = $('#newLatitude').val();


    if(networkName == "" && longitude == "" && latitude == ""){
        createToastMsg("Please make sure all entries are complete!", "red")
    }else{
        var networkNode = {
            node_name: networkName,
            node_location: "Unknown",
            longitude : longitude,
            latitude: latitude
        };
        var jsonData = JSON.stringify(networkNode);

        $('#modalSpinner').css("visibility", "visible");
        $.ajax({
            type: "POST",
            url: "http://localhost:8081/api-v1/nodes",
            data: jsonData,
            contentType: "application/json; charset=utf-8",
            success: function(){
                $('#myModal').modal('toggle');
                createToastMsg("The network node has been created!", "green")
                $('#modalSpinner').css("visibility", "hidden");
                queryNetworkNodes();
            },
            error: function() {
                $('#myModal').modal('toggle');
                createToastMsg("The network node has not been created!", "red")
                $('#modalSpinner').css("visibility", "hidden");
                queryNetworkNodes();
            }
        });

    }
}

function handleEdit(id){

    var networkName = $('#editNetworkName').val();
    var longitude = $('#editLongitude').val();
    var latitude = $('#editLatitude').val();

    if(networkName == "" && longitude == "" && latitude == ""){
        createToastMsg("Please make sure all entries are complete!", "red")
    }else{
        var networkNode = {
            node_name: networkName,
            node_location: "Unknown",
            longitude : longitude,
            latitude: latitude
        };
        var jsonData = JSON.stringify(networkNode);

        $('#modalSpinner').css("visibility", "visible");
        $.ajax({
            type: "PUT",
            url: "http://localhost:8081/api-v1/nodes/" + id,
            data: jsonData,
            contentType: "application/json; charset=utf-8",
            success: function(){
                $('#myModal').modal('toggle');
                createToastMsg("The network node has been changed!", "green")
                $('#modalSpinner').css("visibility", "hidden");
                queryNetworkNodes();
            },
            error: function() {
                $('#myModal').modal('toggle');
                createToastMsg("The network node has not been changed!", "red")
                $('#modalSpinner').css("visibility", "hidden");
                queryNetworkNodes();
            }
        });
    }
}

function handleDelete(id){
    $.ajax({
        type: "DELETE",
        url: "http://localhost:8081/api-v1/nodes/" + id,
        success: function(){
            $('#myModal').modal('toggle');
            createToastMsg("The network node has been deleted!", "green")
            queryNetworkNodes();
        },
        error: function() {
            $('#myModal').modal('toggle');
            createToastMsg("The network node has not been deleted!", "red")
            queryNetworkNodes();
        }
    });
}

function handleSearch(){
    var searchNodeID = $('#findNodeID').val();
    if(Number.isInteger(parseInt(searchNodeID))){
        $.ajax({
            type: "GET",
            url: "http://localhost:8081/api-v1/nodes/" + searchNodeID,
            success: function(response){
                $('#cardsPage').empty();
                $('#cardsPage').append(newCard(response.id, response.node_name, response.node_location, response.latitude, response.longitude))
            },
            error: function() {
                createToastMsg("The network node has not found with " + searchNodeID, "red")
                queryNetworkNodes();
            }
        });
    }
    else{
        createToastMsg("Enter a valid node ID", "red")
    }
}

function newCard(id,name,location,latitude, longitude){
    return (

        '<div class="col" style="padding-top: 2em">'+
        '<div class="card" style="width: 18rem;">'+
        '<div class="card-body">'+
        '<div class="container">'+
        '<div class="row">'+
        '<div class="col-*-*" style="font-weight: bold">ID:</div>'+
        '<div class="col-*-*" style="padding-left: 1em">'+ id +'</div>'+
        '</div>'+
        '<div class="row">'+
        '<div class="col-*-*" style="font-weight: bold">Name: </div>'+
        '<div class="col-*-*" style="padding-left: 1em">'+name+'</div>'+
        '</div>'+
        '<div class="row">'+
        '<div class="col-*-*" style="font-weight: bold">Location: </div>'+
        '<div class="col-*-*" style="padding-left: 1em">'+location+'</div>'+
        '</div>'+
        '<div class="row">'+
        '<div class="col-*-*" style="font-weight: bold">Latitude: </div>'+
        '<div class="col-*-*" style="padding-left: 1em">'+latitude+'</div>'+
        '</div>'+
        '<div class="row">'+
        '<div class="col-*-*" style="font-weight: bold">Longitude: </div>'+
        '<div class="col-*-*" style="padding-left: 1em">'+longitude+'</div>'+
        '</div>'+
        '<br>'+
        '<div class="row">'+
        '<div class="col-*-*" style="font-weight: bold">'+
        '<a href="#" class="btn btn-primary" onclick="showModalPopUp(\'' + latitude + '\',\'' + longitude + '\')">'+
        '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-map" viewBox="0 0 16 16">'+
        '<path fill-rule="evenodd" d="M15.817.113A.5.5 0 0 1 16 .5v14a.5.5 0 0 1-.402.49l-5 1a.5.5 0 0 1-.196 0L5.5 15.01l-4.902.98A.5.5 0 0 1 0 15.5v-14a.5.5 0 0 1 .402-.49l5-1a.5.5 0 0 1 .196 0L10.5.99l4.902-.98a.5.5 0 0 1 .415.103M10 1.91l-4-.8v12.98l4 .8zm1 12.98 4-.8V1.11l-4 .8zm-6-.8V1.11l-4 .8v12.98z"/>'+
        '</svg>'+
        '</a>'+
        '</div>'+
        '<div class="col-*-*" style="padding-left: 1em">'+
        '<a href="#" class="btn btn-warning" onclick="editModalPopUp('+id+')">'+
        '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-pen" viewBox="0 0 16 16">'+
        '<path d="m13.498.795.149-.149a1.207 1.207 0 1 1 1.707 1.708l-.149.148a1.5 1.5 0 0 1-.059 2.059L4.854 14.854a.5.5 0 0 1-.233.131l-4 1a.5.5 0 0 1-.606-.606l1-4a.5.5 0 0 1 .131-.232l9.642-9.642a.5.5 0 0 0-.642.056L6.854 4.854a.5.5 0 1 1-.708-.708L9.44.854A1.5 1.5 0 0 1 11.5.796a1.5 1.5 0 0 1 1.998-.001m-.644.766a.5.5 0 0 0-.707 0L1.95 11.756l-.764 3.057 3.057-.764L14.44 3.854a.5.5 0 0 0 0-.708z"/>'+
        '</svg>'+
        '</a>'+
        '</div>'+
        '<div class="col-*-*" style="padding-left: 1em">'+
        '<a href="#" class="btn btn-danger" onclick="deleteModalPopUp('+id+')">'+
        '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-trash3" viewBox="0 0 16 16">'+
        '<path d="M6.5 1h3a.5.5 0 0 1 .5.5v1H6v-1a.5.5 0 0 1 .5-.5M11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3A1.5 1.5 0 0 0 5 1.5v1H1.5a.5.5 0 0 0 0 1h.538l.853 10.66A2 2 0 0 0 4.885 16h6.23a2 2 0 0 0 1.994-1.84l.853-10.66h.538a.5.5 0 0 0 0-1zm1.958 1-.846 10.58a1 1 0 0 1-.997.92h-6.23a1 1 0 0 1-.997-.92L3.042 3.5zm-7.487 1a.5.5 0 0 1 .528.47l.5 8.5a.5.5 0 0 1-.998.06L5 5.03a.5.5 0 0 1 .47-.53Zm5.058 0a.5.5 0 0 1 .47.53l-.5 8.5a.5.5 0 1 1-.998-.06l.5-8.5a.5.5 0 0 1 .528-.47M8 4.5a.5.5 0 0 1 .5.5v8.5a.5.5 0 0 1-1 0V5a.5.5 0 0 1 .5-.5"/>'+
        '</svg>'+
        '</a>'+
        '</div>'+
        '</div>'+
        '</div>'+
        '</div>'+
        '</div>'+
        '</div>'
    );
}

function editModalPopUp(id){
    $.ajax({
        type: "GET",
        url: "http://localhost:8081/api-v1/nodes/" + id,
        success: function(response){

            $('#myModalLabel').empty();
            $('#myModalLabel').html("Edit Node");

            $('#modalBody').empty();
            $('#modalBody').append(
                '<form>'+
                '<div class="form-group">'+
                '<label for="newNetworkName">Edit Network Name</label>'+
                '<input type="text" class="form-control" id="editNetworkName" value='+response.node_name+'>'+
                '</div>'+
                '<div class="form-group">'+
                '<label for="newLatitude">Edit Latitude</label>'+
                '<input type="text" class="form-control" id="editLatitude" value='+response.latitude+'>'+
                '</div>'+
                '<div class="form-group">'+
                '<label for="newLongitude">Edit Longitude</label>'+
                '<input type="text" class="form-control" id="editLongitude" value='+response.longitude+'>'+
                '</div>'+
                '</form>'
            );

            $('#modalFooter').empty();
            $('#modalFooter').append(
                '<div id="modalSpinner" class="spinner-border" style="width: 3rem; height: 3rem; visibility: hidden;" role="status">'+

                '</div>'+
                '<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>' +
                '<button type="button" class="btn btn-success" onclick="handleEdit('+id+')">Make Changes</button>'
            );

            $('#myModal').modal('show');
        },
        error: function() {
            createToastMsg("Failed to find a the node by " + id, "red")
        }
    });


}

function deleteModalPopUp(id){
    $('#myModalLabel').empty();
    $('#myModalLabel').html("Delete Node");

    $('#modalBody').empty();
    $('#modalBody').append(
        '<p>' +
        'Are you sure you want to delete this node'+
        '</p>'
    );

    $('#modalFooter').empty();
    $('#modalFooter').append(
        '<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>'+
        '<button type="button" class="btn btn-danger" onclick="handleDelete('+id+')">Confirm Delete</button>'
    );

    $('#myModal').modal('show');
}

function createNodeModalPopUp(){
    $('#myModalLabel').empty();
    $('#myModalLabel').html("Create New Node");

    $('#modalBody').empty();
    $('#modalBody').append(
        '<form>'+
        '<div class="form-group">'+
        '<label for="newNetworkName">New Network Name</label>'+
        '<input type="text" class="form-control" id="newNetworkName">'+
        '</div>'+
        '<div class="form-group">'+
        '<label for="newLatitude">New Latitude</label>'+
        '<input type="text" class="form-control" id="newLatitude">'+
        '</div>'+
        '<div class="form-group">'+
        '<label for="newLongitude">New Longitude</label>'+
        '<input type="text" class="form-control" id="newLongitude">'+
        '</div>'+
        '</form>'
    );

    $('#modalFooter').empty();
    $('#modalFooter').append(
        '<div id="modalSpinner" class="spinner-border" style="width: 3rem; height: 3rem; visibility: hidden;" role="status">'+

        '</div>'+
        '<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>'+
        '<button type="button" class="btn btn-success" onclick="handleAdding()">Create New Node</button>'
    );

    $('#myModal').modal('show');
}

function showModalPopUp(lat, long){
    $('#myModalLabel').empty();
    $('#myModalLabel').html("Show Network Node Map");

    $('#modalBody').empty();
    $('#modalBody').append(
       '<img src="https://maps.googleapis.com/maps/api/staticmap?center=' +
        + lat +','+ long +
        '&zoom=12' +
        '&size=600x400&maptype=roadmap&markers=color:red%7Clabel:N%7C' +
        + lat +','+ long +
        '&key=AIzaSyBvyi-ZxYA0-x8hmMolKY9f_wjAK8xplhc"  width="760" height="520">'
    );

    $('#modalFooter').empty();
    $('#modalFooter').append(
    '<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>'
)
    ;

    $('#myModal').modal('show');
}
function createToastMsg(msg, colour){
    Toastify({
        text: msg,
        duration: 3000,
        newWindow: true,
        close: true,
        gravity: "top",
        position: "center",
        style: {
            background: colour,
        },
    }).showToast();
}
