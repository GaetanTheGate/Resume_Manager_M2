<!-- If list isn't empty -->
<div v-if="(show != null)">
    <h2><b>{{show.title}}</b></h2>
    <div class="margin_small">
        <table class="table">
            <tr class ="thead-dark">
                <th v-for="(item, key, index) in show.fields">{{item}}</th>
            </tr>
            <tr v-for="element in list" v-on:click="show.onClick(element.id)" class="clickable">
                <td v-for="(item, key, index) in show.fields">{{element[key]}}</td>
            </tr>
        </table>
    </div>
</div>
<!-- If list is empty -->
<div v-if="show == null">
</div>