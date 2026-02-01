// @ts-check
import { defineConfig } from 'astro/config';
import starlightSocialIcons from './src/utils/socialIcons.ts';
import starlight from '@astrojs/starlight';

import aiRobotsTxt from 'astro-ai-robots-txt';

// https://astro.build/config
export default defineConfig({
	site: process.env.CUSTOM_SITE_ENV || undefined,
	base: process.env.CUSTOM_BASE_ENV || undefined,
	integrations: [
		starlightSocialIcons({
			modrinth: "https://modrinth.com/mod/mesh-lib"
		}),
		starlight({
			title: 'MESH Lib',
			credits: true,
			logo: {
				src: './src/assets/face.svg'
			},
			customCss: [
				'./src/styles/custom.css'
			],
			components: {
				SocialIcons: './src/utils/SocialIcons.astro',
				PageFrame: './src/utils/PageFrame.astro'
			},
			social: [
				{icon: 'github', label: 'GitHub', href: 'https://github.com/OffsetMods538/MESH-Lib'},
				{icon: 'discord', label: 'Discord', href: 'https://discord.offsetmonkey538.top'}
			],
			sidebar: [
				{
					label: 'Guides',
					items: [
						// Each item here is one entry in the navigation menu.
						{ label: 'Example Guide', slug: 'guides/example' },
					],
				},
				{
					label: 'Reference',
					autogenerate: { directory: 'reference' },
				},
			],
		}),
		aiRobotsTxt()
	]
});
