//
//  MyViewController.m
//  HelloWorld
//
//  Created by Mauricio Leal on 1/11/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "MyViewController.h"
#import "AutoLayout.h"

@interface MyViewController()
@property (nonatomic, strong) UIBarButtonItem *buttonLoad, *buttonRight;
@property (nonatomic, strong) UIWebView *webView;

@end

@implementation MyViewController

-(void)viewDidLoad {
    [super viewDidLoad];
    
    _buttonLoad = [[UIBarButtonItem alloc] initWithTitle:@"Load" style:UIBarButtonItemStylePlain target:self action:@selector(actionLoad:)];
    _buttonRight = [[UIBarButtonItem alloc] initWithTitle:@"Right" style:UIBarButtonItemStylePlain target:self action:@selector(actionRight:)];
    [self.navigationItem setLeftBarButtonItem:_buttonLoad];
    [self.navigationItem setRightBarButtonItem:_buttonRight];
    [self.navigationItem setTitle:@"Testing UIWebView"];
    
    _webView = [[UIWebView alloc] initWithFrame:self.view.bounds];
    [self.webView setTranslatesAutoresizingMaskIntoConstraints:NO];
    [self.view addSubview:self.webView];
    [AutoLayout fill:self.webView inside:self.view];
}

-(void)actionLoad:(id)sender {
    NSLog(@"[MyViewController actionLoad] START");
    [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:@"http://google.com"]]];
    NSLog(@"[MyViewController actionLoad]   END");
}

-(void)actionRight:(id)sender {
    NSLog(@"[MyViewController actionRight]");
    
}

@end
