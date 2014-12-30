<%--
  Created by IntelliJ IDEA.
  User: pisx
  Date: 2014/12/25
  Time: 10:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>文件上传</title>
</head>
<script type="text/javascript" src="static/js/jquery-1.11.2.js"></script>
<script>
    jQuery(document).ready(function () {
        //检查浏览器是否支持File Api提供的所有功能，如果你只需要部分功能，可以省略其它部分的检查.
        if (window.File && window.FileReader && window.FileList && window.Blob) {
            //全部支持
            console.log("支持Html5!!!");
        } else {
            alert('该浏览器不全部支持File APIs的功能');
        }
    })
</script>
<body>
    <article>
        <div style="padding: 30px;border: solid;width: 300px;">
            <input type="file" id="files" name="files[]" multiple/>
            <output id="list"></output>
        </div>
    </article>

    <script>
        function handleFileSelect(evt) {
            var files = evt.target.files; // FileList object
            // files is a FileList of File objects. List some properties.
            var output = [];
            for (var i = 0, f; f = files[i]; i++) {
                output.push('<li><strong>', f.name, '</strong> (', f.type || 'n/a', ') - ',
                        f.size, ' bytes, last modified: ',
                        f.lastModifiedDate.toLocaleDateString(), '</li>');
            }
            document.getElementById('list').innerHTML = '<ul>' + output.join('') + '</ul>';
        }
        document.getElementById('files').addEventListener('change', handleFileSelect, false);
    </script>


    <table style="padding: 50px;" width="485" border="1" cellspacing=0 cellpadding=5>
        <tr bgcolor="#F2F2F2">
            <td class="tableheader" align="left">
                <p>Thumbnail palette</p>
            </td>
        </tr>
        <tr>
            <td align="left" height="105" ondragenter="return false" ondragover="return false" ondrop="dropIt(event)">
                <output id="thumbs"></output>
            </td>
        </tr>
        <tr>
            <td align="center">
                <p>Drag & drop or choose images from your local file system: <input type="file" id="input" size="10" multiple="true" onchange="imagesSelected(this.files)" /></p>
            </td>
        </tr>
    </table>
    <script>
        function imagesSelected(myFiles){
            for (var i = 0, f; f = myFiles[i]; i++){
                var imageReader = new FileReader();
                imageReader.onload = (function(aFile){
                    return function(e){
                        var span = document.createElement('span');
                        span.innerHTML =
                                ['<img class="images" src="', e.target.result,'" title="', aFile.name,'"/>'].join('');
                        document.getElementById('thumbs').insertBefore(span, null);
                    };
                })(f);
                imageReader.readAsDataURL(f);
            }
        }

        function dropIt(e){
            imagesSelected(e.dataTransfer.files);
            e.stopPropagation();
            e.preventDefault();
        }
    </script>
</body>
</html>
