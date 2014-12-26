<%--
  Created by IntelliJ IDEA.
  User: pisx
  Date: 2014/12/26
  Time: 11:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <script type="text/javascript" src="js/spark-md5.min.js"></script>
</head>
<body>
<input type="file" id="files" name="file"/> Read bytes:
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
    blobSlice = File.prototype.mozSlice || File.prototype.webkitSlice || File.prototype.slice
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
//        console.log(File.prototype)
//        console.log(blobSlice)

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

    chunkSize = 200 * 1024;//块大小
    function getMD5(file){
        if(!(file instanceof File)){
            console.log("非文件！");
            return null;
        }
        var spark = new SparkMD5.ArrayBuffer();


        //计算文件大小 并分片
        var chunkNums = Math.ceil(file.size/chunkSize);//分片的总数量
        var hexHash;

        if(chunkNums >= 3){
            //取头 中 尾部
            var head_start = 0;
            var end_start = (chunkNums-1)*chunkSize;
            var mid_start = (Math.floor(chunkNums/2))*chunkSize;
            var aaa = [head_start,mid_start,end_start];
            for(var i=0;i<3;i++){
                var reader = new FileReader();
                reader.onloadend = function (evt) {
                    if (evt.target.readyState == FileReader.DONE) { // DONE == 2
                        console.log(reader.result);
                        spark.append(reader.result);
//                        reader.abort();
                    }
                }
                var last = aaa[i] + chunkSize;
                last = last > file.size ? file.size : last;
                reader.readAsArrayBuffer(blobSlice.call(file, aaa[i], last));
            }
//            reader.readAsArrayBuffer(blobSlice.call(file, head_start, head_start + chunkSize))
//            reader.readAsArrayBuffer(blobSlice.call(file, mid_start, mid_start + chunkSize))
//            reader.readAsArrayBuffer(blobSlice.call(file, end_start, file.size))

        }else if(chunkNums>=1){
            var reader = new FileReader();
            reader.onloadend = function (evt) {
                if (evt.target.readyState == FileReader.DONE) { // DONE == 2
                    console.log(reader.result);
                    spark.append(reader.result);
//                        reader.abort();
                }
            }
            reader.readAsArrayBuffer(file)
        }else{
            hexHash = "";
        }
        hexHash = spark.end();
        return hexHash;
    }

    function handleFileSelect(evt) {
        var files = evt.target.files; // FileList object
        // files is a FileList of File objects. List some properties.
        var output = [];
        for (var i = 0, f; f = files[i]; i++) {
            var hexHash = getMD5(files[i]);
            output.push('<li><strong>', f.name, '</strong> (', f.type || 'n/a', ') - ',
                    f.size, ' bytes, last modified: ',
                    f.lastModifiedDate.toLocaleDateString(), ' hexHash: ', hexHash, '</li>');
        }
        document.getElementById('list').innerHTML = '<ul>' + output.join('') + '</ul>';
    }
    document.getElementById('files').addEventListener('change', handleFileSelect, false);
</script>
</body>
</html>
