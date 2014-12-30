<%--
  Created by IntelliJ IDEA.
  User: pisx
  Date: 2014/12/26
  Time: 11:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="WEB-INF/view/path.jsp"%>
<html>
<head>
    <title>读取文件</title>
    <style>
        #byte_content {
            margin: 5px 0;
            max-height: 100px;
            overflow-y: auto;
            overflow-x: hidden;
        }

        #byte_range {
            margin-top: 5px;
        }
    </style>
    <script type="text/javascript" src="<%=basePath%>/static/js/spark-md5.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/static/js/jquery-1.11.2.js"></script>
</head>
<body>
<input type="file" id="files" name="file"/>
<input type="button" id="uploadBtn" value="上传"><br>
Read bytes:
<span class="readBytesButtons">
  <button data-startbyte="0" data-endbyte="4">1-5</button>
  <button data-startbyte="5" data-endbyte="14">6-15</button>
  <button data-startbyte="6" data-endbyte="7">7-8</button>
  <button>entire file</button>
</span>

<div id="byte_range"></div>
<div id="byte_content"></div>
<output id="list"></output>
<script>


    $(function () {
        $("#uploadBtn").on("click",handleClickUploadBtn);
    });
    //文件上传
    function handleClickUploadBtn(e) {
        var files = $("#files")[0].files;
        var file = files[0];

    }

    //上传并验证md5
    function matchMd5(md5) {
        $.ajax({
            type: "POST",
            url: "matchMd5",
//            enctype: 'multipart/form-data',
            dataType: "json",
            data: {
                md5: md5
            },
            success: function (msg) {
                console.log(typeof msg);
            }
        });
    }

    blobSlice = File.prototype.mozSlice || File.prototype.webkitSlice || File.prototype.slice
    chunkSize = 1 * 1024 * 1024;//块大小 字节
    md5 = null;
    function getMD5(file){
        if(!(file instanceof File)){
            console.log("非文件！");
            return null;
        }
        var spark = new SparkMD5.ArrayBuffer();

        //计算文件大小 并分片
        chuck_num = Math.ceil(file.size/chunkSize);//分片的总数量
        currentChunkPoint = 0;
        read_chunk_points = new Array();

        if(chuck_num >= 3){
            var head_start = 0;
            var end_start = (chuck_num - 1) * chunkSize;
            var mid_start = (Math.floor(chuck_num / 2)) * chunkSize;
            read_chunk_points[0] = head_start;
            read_chunk_points[1] = mid_start;
            read_chunk_points[2] = end_start;
        }else{
            read_chunk_points[0] = 0;
        }

        frOnloadEnd = function (e) {
            console.log("read chunk : " , read_chunk_points[currentChunkPoint], "in - ", read_chunk_points.join(","));
            spark.append(e.target.result);
            currentChunkPoint ++;
            if(currentChunkPoint < read_chunk_points.length){
                loadNext(file)
            } else {
                md5 = spark.end();
                //上传md5 并验证md5
                matchMd5(md5);
                console.log("finished loading!")
                console.info("hash : ", md5);
            }
        }
        frOnerror = function () {
            console.warn("oops, something went wrong.");
        };

        loadNext(file);
    }

    function loadNext(file){
        var fileReader = new FileReader();
        fileReader.onloadend = frOnloadEnd;
        fileReader.onerror = frOnerror;

        var start = read_chunk_points[currentChunkPoint],
                end = ((start + chunkSize) >= file.size) ? file.size : start + chunkSize;

        fileReader.readAsArrayBuffer(blobSlice.call(file, start, end));
    }

    function handleFileSelect(evt) {
        var files = evt.target.files; // FileList object
        // files is a FileList of File objects. List some properties.
        var output = [];
        for (var i = 0, f; f = files[i]; i++) {
            getMD5(files[i]);
            output.push('<li><strong>', f.name, '</strong> (', f.type || 'n/a', ') - ',
                    f.size, ' bytes, last modified: ',
                    f.lastModifiedDate.toLocaleDateString(), '</li>');
        }
        document.getElementById('list').innerHTML = '<ul>' + output.join('') + '</ul>';
    }
    document.getElementById('files').addEventListener('change', handleFileSelect, false);

    function uploadFile(file){
        var oXHR = new XMLHttpRequest();

    }

    function readBlob(opt_startByte, opt_stopByte) {
        var files = document.getElementById('files').files;
        if (!files.length) {
            alert('Please select a file!');
            return;
        }

        var file = files[0];
        var start = parseInt(opt_startByte) || 0;
        var stop = parseInt(opt_stopByte) || file.size - 1;

        var reader = new FileReader();

        // If we use onloadend, we need to check the readyState.
        reader.onloadend = function (evt) {
            console.log(evt)
            if (evt.target.readyState == FileReader.DONE) { // DONE == 2
                document.getElementById('byte_content').textContent = evt.target.result;
                document.getElementById('byte_range').textContent =
                        ['Read bytes: ', start + 1, ' - ', stop + 1,
                            ' of ', file.size, ' byte file'].join('');
            }
        };

        var blob = blobSlice.call(file, start, stop + 1);//切片
        reader.readAsBinaryString(blob);
    }

    document.querySelector('.readBytesButtons').addEventListener('click', function (evt) {
        if (evt.target.tagName.toLowerCase() == 'button') {
            var startByte = evt.target.getAttribute('data-startbyte');
            var endByte = evt.target.getAttribute('data-endbyte');
            readBlob(startByte, endByte);
        }
    }, false);
</script>
</body>
</html>
