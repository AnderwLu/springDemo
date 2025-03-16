class TemplateLoader {
    constructor() {
        this.templates = new Map();
        this.templatesContainer = null;
    }

    async loadTemplate(path, id) {
        try {
            const response = await fetch(path);
            const content = await response.text();

            const template = document.createElement('script');
            template.id = id;
            template.type = 'text/x-template';
            template.innerHTML = content;

            this.templates.set(id, template);
            return template;
        } catch (error) {
            console.error(`加载模板失败 ${path}:`, error);
            throw error;
        }
    }

    async loadTemplates() {
        try {
            const templateConfigs = [
                { path: '/springdemo/templates/dashboard.html', id: 'dashboard-template' },
                { path: '/springdemo/templates/home.html', id: 'home-template' },
                { path: '/springdemo/templates/settings.html', id: 'settings-template' },
                { path: '/springdemo/templates/user/list.html', id: 'user-list' }
            ];

            // 创建批处理模板
            const batchTemplate = document.createElement('script');
            batchTemplate.id = 'batch-template';
            batchTemplate.type = 'text/x-template';
            batchTemplate.innerHTML = (await import('./components/batch/template.js')).default;
            this.templates.set('batch-template', batchTemplate);

            // 加载其他模板
            await Promise.all(templateConfigs.map(config => this.loadTemplate(config.path, config.id)));

            this.appendTemplatesToDOM();
        } catch (error) {
            console.error('加载模板失败:', error);
            throw error;
        }
    }

    appendTemplatesToDOM() {
        if (!this.templatesContainer) {
            this.templatesContainer = document.createElement('div');
            this.templatesContainer.id = 'templates';
            this.templatesContainer.style.display = 'none';
            document.body.appendChild(this.templatesContainer);
        }

        this.templates.forEach(template => {
            this.templatesContainer.appendChild(template);
        });
    }
}

export default new TemplateLoader();
