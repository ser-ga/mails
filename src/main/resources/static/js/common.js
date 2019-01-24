let form, ajaxUrl, datatableApi;

ajaxUrl = "rest/mails/";

form = $('#detailsForm');

function add() {
    $("#modalTitle").html('Новое письмо');
    form.find(":input").val("");
    $("#newMail").modal();
}

$.ajaxSetup({cache: false});

function save() {
    let data = $('form#detailsForm').serialize();
    $.ajax({
        type: "POST",
        url: ajaxUrl,
        data: data
    }).done(function () {
        $("#newMail").modal("hide");
        updateTable();
        successNoty("Письмо сохранено");
    });
}

function updateTable() {
    $.ajax({
        type: "GET",
        url: ajaxUrl
    }).done(function (data) {
        datatableApi.clear().rows.add(data).draw();
    });
}

function successNoty(message) {
    new Noty({
        text: "<span class='fa fa-lg fa-check'></span> &nbsp;" + message,
        type: 'success',
        layout: "bottomRight",
        timeout: 1000
    }).show();
}

$(document).ajaxError(function (event, jqXHR, options, jsExc) {
    new Noty({
        text: "<span class='fa fa-lg fa-exclamation-circle'></span> &nbsp;" + "Ошибка " + jqXHR.status,
        type: "error",
        layout: "bottomRight",
        timeout: 3000
    }).show();
});

$(function () {
    datatableApi = $("#mails").DataTable({
        "ajax": {
            "url": ajaxUrl,
            "dataSrc": ""
        },

        "columns": [
            {
                "data": "mailNumber"
            },
            {
                "data": "createDate",
                "render": function (data, type, row) {
                    if (type === "display") {
                        return data.substring(0, 10) + " " + data.substring(11, 16);
                    }
                    return data;
                }
            },
            {
                "data": "mailRecipient"
            },
            {
                "data": "mailSubject"
            },
            {
                "data": "author",
                "render": function (data, type, row) {
                    if (type === "display") {
                        return "<a onclick='change(" + row.id + "," + data.id + "," + row.mailNumber + ");'>" + data.fullName + "</a>";
                    }
                    return data;
                }
            },
            {
                "data": "accept",
                "render": function (data, type, row) {
                    if (type === "display") {
                        return "<input type='checkbox' " + (data ? "checked" : "") + " onclick='acceptMail($(this)," + row.id + ");'/>";
                    }
                    return data;
                }
            },
            {
                "render": renderEditBtn,
                "defaultContent": "",
                "orderable": false
            },
            {
                "render": renderDeleteBtn,
                "defaultContent": "",
                "orderable": false
            },
            {
                "render": renderPdfBtn,
                "defaultContent": "",
                "orderable": false
            }
        ],
        "createdRow": function (row, data, dataIndex) {
            let dataAuthorId = data.author.id.toString();
            let authorId = $('#authorId').html();
            let check = false;
            if (dataAuthorId === authorId) check = true;
            $(row).attr("data-author", check);
        },
        "sDom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
            "<'row'<'col-md-6'><'col-md-6'>>" +
            "<'row'<'col-md-12't>><'row'<'col-md-6'ip>>",
        "paging": true,
        "info": true,
        "order": [
            [
                0,
                "desc"
            ]
        ],
        "language": {
            "lengthMenu": "Показывать _MENU_ записей на странице",
            "search": "Поиск:",
            "zeroRecords": "Ничего не найдено",
            "infoEmpty": "Нет данных",
            "info": "Показано с _START_ по _END_ (Всего _TOTAL_)",
            "infoFiltered": " - отфильтровано из _MAX_ записей",
            "paginate": {
                "previous": "Предыдущая",
                "next": "Следующая"
            }
        }
    });
});


function renderEditBtn(data, type, row) {
    if (type === "display") {
        return "<a onclick='updateMail(" + row.id + ");'><span class='fa fa-edit'></span></a>";
    }
}

function renderDeleteBtn(data, type, row) {
    if (type === "display") {
        return "<a onclick='deleteMail(" + row.id + ");'><span class='fa fa-remove'></span></a>";
    }
}

function renderPdfBtn(data, type, row) {
    if (type === "display") {
        return "<a target='_blank' href='" + "/pdf/" + row.id + "'><span class='fa fa-file-pdf-o'></span></a>";
    }
}

function makeEditable() {
    $(".edit-mail").click(function () {
        updateMail($(this).attr("id"));
    });
    $(".delete-mail").click(function () {
        deleteMail($(this).attr("id"));
    });
}

function updateMail(id) {
    $("#modalTitle").html('Редактирование письма');
    $.get(ajaxUrl + id, function (data) {
        $.each(data, function (key, value) {
            form.find("input[name='" + key + "']").val(value);
            form.find("textarea[name='" + key + "']").val(value);
        });
        $('#newMail').modal();
    });
}

function deleteMail(id) {
    bootbox.confirm({
        message: "Удалить письмо?",
        buttons: {
            confirm: {
                label: 'Да',
                className: 'btn-success'
            },
            cancel: {
                label: 'Нет',
                className: 'btn-danger'
            }
        },
        callback: function (result) {
            if (result) {
                $.ajax({
                    url: ajaxUrl + id,
                    type: "DELETE"
                }).done(function () {
                    updateTable();
                    successNoty("Письмо удалено");
                });
            }
        }
    });
}

function acceptMail(chkbox, id) {
    const enabled = chkbox.is(":checked");
//  https://stackoverflow.com/a/22213543/548473
    $.ajax({
        url: ajaxUrl + id,
        type: "POST",
        data: "accept=" + enabled
    }).done(function () {
        chkbox.closest("tr").attr("data-userEnabled", enabled);
        successNoty(enabled ? "Проверено" : "Отменено");
    }).fail(function () {
        $(chkbox).prop("checked", !enabled);
    });
}

function change(mailId, authorId, mailNumber) {
    $("#authorsTitle").html('Исполнители');
    let sel = $("#authors-id");
    sel.empty();
    let authorsForm = $('#authorsForm');
    authorsForm.find("input[name='id']").val(mailId);
    authorsForm.find("input[name='mailNumber']").val(mailNumber);
    $.get("/rest/authors", function (data) {
        $.each(data, function (key, value) {
            let selected = "";
            if(authorId === value.id) selected = "selected";
            sel.append('<option value="' + value.id + '"' + selected + '>' + value.fullName + '</option>');
        });
        $('#authors').modal();
    });
}

function changeAuthor() {
    const authorsForm = $('#authorsForm');
    let newAuthor = authorsForm.find("select[name='authorId']").val();
    let mailId = authorsForm.find("input[name='id']").val();
    $.ajax({
        url: ajaxUrl + mailId + "/author",
        type: "POST",
        data: "authorId=" + newAuthor
    }).done(function () {
        $("#authors").modal("hide");
        updateTable();
        successNoty("Письмо сохранено");
    });
}
