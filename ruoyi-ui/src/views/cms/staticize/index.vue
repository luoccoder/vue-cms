<template>
  <div class="app-container">
    <el-tabs v-model="activeName">
      <el-tab-pane :label="$t('CMS.Staticize.Tag')" name="tag">
        <cms-template-tag></cms-template-tag>
      </el-tab-pane>
      <el-tab-pane :label="$t('CMS.Staticize.Func')" name="function">
        <cms-template-function></cms-template-function>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import CMSTemplateTag from '@/views/cms/staticize/tagList';
import CMSTemplateFunc from '@/views/cms/staticize/funcList';

export default {
  name: "CMSStaticize",
  components: {
    'cms-template-tag': CMSTemplateTag,
    'cms-template-function': CMSTemplateFunc,
  },
  data () {
    return {
      activeName: 'tag'
    };
  }
};
</script>
<style scoped>
.el-tabs__header {
  margin-bottom: 10px;
}
</style>