function url_set_search(key,value,clear){
            var search = window.location.search;
            var pathname = window.location.pathname;
            //alert(search+ '||'+pathname );
            var keyValue = new Array();
            if(search && search.charAt(0) == '?'){
                search = search.substr(1);
            }
            var flag = false;
            if(search && !clear){
                var arr = search.split('&');
                for(var i =0;i<arr.length;i++){
                    var tmp = arr[i].split('=');
                    if(tmp[0]==key){
                        if(value){
                            keyValue.push(key+"="+encodeURIComponent(value));
                        }
                        flag = true;
                    }
                    else{
                        keyValue.push(arr[i]);
                    }
                }
            }
            if(flag == false){
                keyValue.push(key+"="+encodeURIComponent(value));
            }
            search = keyValue.join("&");
            search = search?"?"+search:"";
            window.open(pathname+search,'_self');
            
            if(event){
                if (event.stopPropagation)
                event.stopPropagation();
                if (event.preventDefault)
                event.preventDefault();
                event.cancelBubble = true;
                event.returnValue = false;
            }
            
            return false;
            //window.open(pathname+search);
            //location.href="http://www.baidu.com/";
        }
        
        function url_get_search(key){
            var search = window.location.search;
            if(search && search.charAt(0) == '?'){
                search = search.substr(1);
            }
            if(search){
                var arr = search.split('&');
                for(var i =0;i<arr.length;i++){
                    var tmp = arr[i].split('=');
                    if(tmp[0]==key){
                        return tmp[1];
                    }
                }
            }
            return "";
        }
        function set_per_page(num){
            url_set_search('num',num,true);
        }
        
        function set_page_num(number){
            url_set_search('p',number,false);
        }
        
        function set_sort(column,defValue){
            var order = url_get_search('order');
            var sep = '|';
            if(order){
                order = decodeURIComponent(order);
                order = order.split(sep);
                var order_key = order[0];
                var order_type = order[1];
                if(order_key && order_type && order_key == column){
                    order_type = order_type==1?2:1;
                    url_set_search('order',column+sep+order_type,false);
                    return;
                }
            }
            var order_type = 0;
            if(defValue){
                order_type = defValue=='ASC'?2:1;
            }
            else{
                var order_type = 2;
            }
            url_set_search('order',column+sep+order_type,false);
        }
        
        function set_last_days(num){
            url_set_search('last',num,true);
        }
        
        
        function search(form){
            var key = form.key.value;
            var url = form.base_url.value;
            if(key == '代码'){
                //return false;
            }
            else{
                if(/^(sh|sz){0,1}\d{6}$/.test(key) == true){
                    url = url+"key/"+key+".phtml";
                    window.open(url,"_self");
                    //return true;   
                }
                else{
                    alert('不正确的股票代码');
                    //return false;
                }
            }
            return false;
        }
        
        /*
        function search_focus(form){
            var key = document.search_form.key;
            if(key.value == '代码'){
                key.value = '';
            }
        }
        function search_blur(form){
            var key = document.search_form.key;
            if(key.value == ''){
                key.value = '代码';
            }
        }
        */
        
        function set_last_days(num){
            url_set_search('last',num,true);
        }
        