<template>
  <el-container>
    <el-aside id="aside" v-bind:style="{width: asideWidth+'%'}">
      <Sidebar @listenAsidebarShow="listenAsidebar" @listenPersonWarningShow="listenHistory"></Sidebar>
    </el-aside>
    <el-container>
      <el-header>
        <el-button type="primary" icon="el-icon-download" @click="downLoadExcel">导出
        </el-button>
      </el-header>
      <el-main>
        <el-table
          :data="tableData"
          stripe
          style="width: 100%"
          @selection-change="handleSelectionChange"
        >
          <el-table-column
            type="selection"
            width="50"
          >
          </el-table-column>
          <el-table-column
            prop="id"
            label="序号"
            width="180">
          </el-table-column>
          <el-table-column
            prop="name"
            label="姓名"
            width="180">
          </el-table-column>
          <el-table-column
            prop="area"
            label="区域"
            width="180">
          </el-table-column>
          <el-table-column
            prop="identityCard"
            label="身份证号"
            width="180">
          </el-table-column>
          <el-table-column
            prop="createTime"
            label="时间"
            width="180">
          </el-table-column>
          <el-table-column
            prop="personArchive"
            label="所属名单"
            width="180">
          </el-table-column>
        </el-table>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import Sidebar from '@/views/visit/Sidebar'
import axios from 'axios'

export default {
  data () {
    return {
      asideWidth: 33,
      tableData: null,
      multiSelection: []
    }
  },
  components: {Sidebar},
  methods:
      {
        listenAsidebar (data) {
          if (data) {
            this.asideWidth = 33
          } else {
            this.asideWidth = 3
          }
        },
        listenHistory (data) {
          this.historyList = data
        },
        downLoadExcel () {
          let th = {'id': '序号', 'name': '姓名', 'area': '区域', 'identityCard': '身份证号', 'personArchive': '名单', 'createTime': '时间'}
          let transferData = {'data': JSON.stringify(this.multiSelection), 'th': th}
          axios({
            method: 'post',
            url: '/api/common/createExcel',
            data: transferData,
            headers: {
              'Content-Type': 'application/json'
            }
          }).then(function (response) {
            window.location.href = '/api/common/downloadExcel?filePath=' + response.data + '&fileName=warning.xls'
          })
        },
        handleSelectionChange (val) {
          this.multiSelection = val
        }
      },
  mounted () {
    axios.post('/api/warningcenter/getPersonWarning', {
      name: null,
      area: null,
      cardNo: null,
      startTime: null,
      endTime: null,
      pageNum: null,
      pageSize: null
    }).then(response => {
      this.tableData = response.data
    })
  }
}

</script>

<!-- Add 'scoped' attribute to limit CSS to this component only -->
<style scoped>
  #aside::-webkit-scrollbar {
    display: none;
  }
</style>
